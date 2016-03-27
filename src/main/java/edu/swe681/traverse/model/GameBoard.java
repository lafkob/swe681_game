package edu.swe681.traverse.model;
import java.awt.Point;
import java.util.List;
import java.util.Random;

/**
 * An immutable Traverse game board
 */
public final class GameBoard
{
	/* The size of the board. Note that if we were to add
	 * two other players, we'd have to do quite a bit of adjusting
	 * to the checks. */
	private static final int NUM_ROWS = 10;
	private static final int NUM_COLS = 8;
	private static final int EMPTY = -1;
	
	private int board[][];
	private GameState currentState;
	/* Tracks when jumps have been made in a series of moves */
	private boolean jumpMade;
	
	/**
	 * Default constructor. The game board begins with all
	 * pieces in their starting rows. If shuffle is true,
	 * the starting locations will be randomly shuffled.
	 * 
	 * @param shuffle Shuffles the starting locations if true,
	 * 	does not otherwise
	 */
	public GameBoard(boolean shuffle)
	{
		board = new int[NUM_ROWS][NUM_COLS];
		for (int row = 0; row < NUM_ROWS; row++)
		{
			for (int col = 0; col < NUM_COLS; col++)
			{
				/* Player 1 starting row */
				if (row == 0)
					board[row][col] = col;
				/* Player 2 starting row */
				else if (row == NUM_ROWS - 1)
					board[row][col] = col+NUM_COLS;
				else
					board[row][col] = EMPTY;
			}
		}
		
		/* Shuffle the starting positions for players 1 and 2 */
		if (shuffle)
			shuffleStartingPos();
		
		this.currentState = new GameState(GameStatus.PLAY, Player.ONE);
	}
	
	/**
	 * An internal copy constructor
	 * 
	 * @param oldBoard GameBoard to copy to the new one
	 */
	protected GameBoard(GameBoard oldBoard)
	{
		int[][] newBoard;
		
		newBoard = new int[NUM_ROWS][NUM_COLS];
		for (int row = 0; row < NUM_ROWS; row++)
		{
			for (int col = 0; col < NUM_COLS; col++)
			{
				newBoard[row][col] = oldBoard.board[row][col];
			}
		}
		this.board = newBoard;
		
		this.currentState = new GameState(
				oldBoard.getCurrentState().getStatus(),
				oldBoard.getCurrentState().getPlayer());
				
		this.jumpMade = oldBoard.jumpMade;
	}
	
	/**
	 * Returns the current game state
	 * 
	 * @return The current game state
	 */
	public GameState getCurrentState()
	{
		/* Note: GameStates are immutable, so returning this as is
		 * shouldn't be an issue. */
		return currentState;
	}
	
	/**
	 * Returns a GameBoard that is this with the turn
	 * advanced to the next player
	 * 
	 * @return A GameBoard with the turn advanced
	 */
	public GameBoard advanceTurn()
	{
		GameBoard newBoard = new GameBoard(this);
		
		if (currentState.getPlayer() == Player.ONE)
			newBoard.currentState = newBoard.currentState.updatePlayer(Player.TWO);
		else
			newBoard.currentState = newBoard.currentState.updatePlayer(Player.ONE);
		
		return newBoard;
	}
	
	/**
	 * Returns a GameBoard with the given piece moved from its starting
	 * location to the final destination.
	 * 
	 * @param pieceID Piece to be moved
	 * @param start Piece's starting location
	 * @param dests A series of spaces in the move, ending at the final location
	 * @return A GameBoard with the piece moved to its final location
	 * 
	 * @throws InvalidMoveException If the move(s) are invalid
	 * @throws IllegalMoveException If the move(s) are illegal
	 */
	public GameBoard movePiece(int pieceID, Point start, List<Point> dests)
		throws InvalidMoveException, IllegalMoveException
	{
		GameBoard newBoard;
		Point midway, dest;
		
		newBoard = new GameBoard(this);
		
		/* Make sure the starting location and piece are valid before 
		 * evaluating the destinations. */
		newBoard.validatePieceAndStart(pieceID, start);
		
		newBoard.jumpMade = false;
		
		/* Increments through the destinations and validates each one */
		for (int moveNum = 0; moveNum < dests.size(); moveNum++)
		{
			dest = dests.get(moveNum);
			
			/* The first start of the set of moves is the start itself, but 
			 * for each move after that, the starting point is actually the last 
			 * destination. */
			if (moveNum == 0)
				midway = start;
			else
				midway = dests.get(moveNum - 1);
			
			/* Currently, we will start in the play stage and randomize the
			 * starting pieces. In the future, we may allow players to rearrange
			 * their starting locations. */
			if (currentState.getStatus()== GameStatus.START)
			{
				newBoard.validateStartMove(start, dest);
				
				throw new UnsupportedOperationException("Arranging pieces at the start is not"
						+ "currently implemented.");
				/*
				int temp = newBoard.board[dest.x][dest.y];
				newBoard.board[dest.x][dest.y] = newBoard.board[midway.x][midway.y];
				newBoard.board[midway.x][midway.y] = temp;
				*/
			}
			if (currentState.getStatus()== GameStatus.PLAY)
			{
				/* Every move after the first move must have followed a jump move */
				if (moveNum > 0 && !newBoard.jumpMade)
				{
					throw new IllegalMoveException("You can only move additional spaces if the last "
							+ "move was a jump.", midway, dest);
				}
				
				newBoard.validatePlayMove(pieceID, midway, dest);
				
				newBoard.board[dest.x][dest.y] = newBoard.board[midway.x][midway.y];
				newBoard.board[midway.x][midway.y] = EMPTY;
			}
		}
		
		return newBoard;
	}
	
	/**
	 * Returns a GameBoard with the piece at the given location moved
	 * to the final destination.
	 * 
	 * @param start Piece's starting location
	 * @param dests A series of spaces in the move, ending at the final location
	 * @return A GameBoard with the piece moved to its final location
	 * 
	 * @throws InvalidMoveException If the move(s) are invalid
	 * @throws IllegalMoveException If the move(s) are illegal
	 */
	public GameBoard movePiece(Point start, List<Point> dests)
			throws InvalidMoveException, IllegalMoveException
	{
		/* Added this in to avoid any index-out-of-bound errors */
		if (!(start.x >= 0 && start.x < NUM_ROWS &&
			  start.y >= 0 && start.y < NUM_COLS))
		{
			throw new InvalidMoveException(String.format("Invalid move. The starting location "
					+ "[%d,%d] is not on the board.\n", start.x, start.y));
		}
			
		return this.movePiece(board[start.x][start.y], start, dests);
	}
	
	/**
	 * Validate the given piece and its starting location.
	 * 
	 * @param pieceID Piece to be moved
	 * @param start Piece's starting location
	 * 
	 * @throws InvalidMoveException If the piece or starting location is invalid
	 * @throws IllegalMoveException If the piece or starting location is illegal
	 */
	private void validatePieceAndStart(int pieceID, Point start)
		throws InvalidMoveException, IllegalMoveException
	{
		/* Start is out of bounds */
		if (!(start.x >= 0 && start.x < NUM_ROWS &&
			  start.y >= 0 && start.y < NUM_COLS))
		{
			throw new InvalidMoveException(String.format("Invalid move. The starting location "
					+ "[%d,%d] is not on the board.\n", start.x, start.y));
		}
		
		/* If the piece ID is not 0-15, then something has gone wrong or there is no piece at
		 * that location. */
		if (!(pieceID >= 0 && pieceID <= 15))
		{
			if (pieceID == EMPTY)
				throw new InvalidMoveException(String.format("Invalid move. The location"
						+ " [%d,%d] is empty.\n", start.x, start.y));
			else
				throw new InvalidMoveException(String.format("The given piece ID (%d) is "
						+ "invalid.\n", pieceID));
			
		}
		
		/* Piece is not where the player expects it to be */
		if (board[start.x][start.y] != pieceID)
		{
			throw new InvalidMoveException(String.format("The selected piece and"
					+ " the starting location (%d,%d) do not match.\n", start.x, start.y));
		}
		
		/* That is not the player's piece */
		GamePiece piece = Game.PIECES[pieceID];
		if (piece.getPlayer() != currentState.getPlayer())
		{
			throw new IllegalMoveException(String.format("Illegal piece selection. That is PLAYER %s\'s "
					+ "piece.\n", piece.getPlayer()));
		}
	}
	
	/**
	 * Precondition: Must be called after validatePieceAndStart and during
	 * the starting phase.
	 * Validates the given move taken during the staring phase.
	 * 
	 * @param start Piece's starting location
	 * @param dest Location to which the piece is moving
	 * 
	 * @throws IllegalMoveException If the move is illegal
	 */
	private void validateStartMove(Point start, Point dest)
		throws IllegalMoveException
	{
		int row;
		
		/* If we're just stating out, pieces can be swapped with each other,
		 * but only within their row */
		if (currentState.getPlayer() == Player.ONE)
			row = 0;
		else
			row = NUM_ROWS - 1;
		
		if (start.x != row || dest.x != row)
		{
			throw new IllegalMoveException("Illegal move. You may only move within your "
					+ "starting row.\n");
		}
	}
	
	/**
	 * Precondition: This must be called after validatePieceAndStart and during
	 * the gameplay phase.
	 * Validates a move from the starting location to the new location.
	 * 
	 * @param pieceID Piece to be moved
	 * @param start Piece's starting location
	 * @param dest Location to which the piece is moving
	 * @throws IllegalMoveException If the move is illegal
	 * @throws InvalidMoveException If the move is invalid
	 */
	private void validatePlayMove(int pieceID, Point start, Point dest)
		throws IllegalMoveException, InvalidMoveException
	{
		GamePiece piece;
		int playerSign;
		
		/* Destination or start is out of bounds.
		 * Note: Given that the start is the last destination and has already been checked,
		 * based on how this is used, we could probably make it a precondition that  */
		if (!(start.x >= 0 && start.x < NUM_ROWS &&
			  start.y >= 0 && start.y < NUM_COLS))
		{
			throw new InvalidMoveException(String.format("The location [%d,%d] is not "
					+ "on the board.\n", start.x, start.y), start, dest);
		}
				
		if (!(dest.x >= 0 && dest.x < NUM_ROWS &&
			  dest.y >= 0 && dest.y < NUM_COLS))
		{
			throw new InvalidMoveException(String.format("The location [%d,%d] is not "
					+ "on the board.\n", dest.x, dest.y), start, dest);
		}
		
		/* Probably shouldn't just move the piece to the same location */
		if (start.equals(dest))
		{
			throw new IllegalMoveException("The piece has not been moved.\n", start, dest);
		}
		
		/* Another piece is already at the destination */
		if (board[dest.x][dest.y] >= 0)
		{
			throw new IllegalMoveException("There is already a piece at that location.\n",
					start, dest);
		}
		
		piece = Game.PIECES[pieceID];
		
		/* Check to make sure they're moving only one space or jumping a piece. */
		if (!isMovingLegalDistance(start, dest))
		{
			throw new IllegalMoveException("Pieces may only move one space or jump "
					+ "over another piece.\n", start, dest);
		}
		
		/* Check to make sure the direction is valid */

		/* A square can move horizontally or vertically */
		else if (piece.getPieceType() == GamePieceType.SQUARE)
		{
			/* Valid direction for a square? Note: the format of these
			 * is - if the condition is not met, then this is illegal. */
			if (!(start.x == dest.x || start.y == dest.y))
			{
				throw new IllegalMoveException("A square may only move verically or "
						+ "horizontally.\n", start, dest);
			}
		}
		
		/* Diamonds can move only diagonally */
		else if (piece.getPieceType() == GamePieceType.DIAMOND)
		{
			/* Valid direction for a diamond? */
			if (!(Math.abs(dest.x - start.x) == Math.abs(dest.y - start.y)))
			{
				throw new IllegalMoveException("A diamond may only move diagonally.\n",
						start, dest);
			}
		}
		/* Triangles can move forward diagonally or straight backwards */
		else if (piece.getPieceType() == GamePieceType.TRIANGLE)
		{
			/* Determine which direction we're facing */
			if (piece.getPlayer() == Player.ONE)
				playerSign = 1;
			else
				playerSign = -1;
			
			/* Valid direction for a triangle? Note that since forwards
			 * and backwards are relevant, the player sign flips the
			 * forward and backward checks as necessary. */
			if (!(start.y == dest.y && (dest.x - start.x)*playerSign < 0) &&
				!(Math.abs(dest.x - start.x) == Math.abs(dest.y - start.y) &&
				 (dest.x - start.x)*playerSign > 0))
			{
				throw new IllegalMoveException("A triangle may only move forward "
						+ "diagonally or straight backwards.\n", start, dest);
			}
		}
		
		/* Otherwise, the piece is a circle, and circles can move in any direction. */
	}
	
	@Override
	public String toString()
	{
		String ret = "";
		GamePiece piece;
		int id;
		boolean showSymbol, showID;
		
		showSymbol = true;
		showID = false;
		
		for (int row = 0; row < NUM_ROWS; row++)
		{
			for (int col = 0; col < NUM_COLS; col++)
			{
				id = board[row][col];
				if (id < 0)
				{
					if (showID || !showSymbol)
						ret += " ";
					
					ret += "-";
					
					if (showID && showSymbol)
						ret += " ";
				}
	
				else
				{
					piece = Game.PIECES[id];
					if (showSymbol)
					{
						if (piece.getPieceType() == GamePieceType.CIRCLE)
							ret += "O";
						else if (piece.getPieceType() == GamePieceType.SQUARE)
							ret += "#";
						else if (piece.getPieceType() == GamePieceType.DIAMOND)
							ret += "+";
						else
							ret += "^";
					}
					if (showID || !showSymbol)
					ret += String.format("%2d", id);
				}
				
				if (col != NUM_COLS - 1)
					ret += " ";
			}
			ret += "\n";
		}
		
		return ret;
	}
	
	/**
	 * Returns true if moving from the starting point to the destination
	 * is a legal distance (one space or two spaces if jumping another
	 * piece).
	 * 
	 * @param start Piece starting location
	 * @param dest Piece destination location
	 * @return True if the move is legal, false otherwise
	 */
	private boolean isMovingLegalDistance(Point start, Point dest)
		throws IllegalMoveException
	{
		int signNumX, signNumY;
		
		/* Any attempts at moves greater than 2 are invalid */
		if (Math.abs(dest.x - start.x) > 2 ||
			Math.abs(dest.y - start.y) > 2)
		return false;
		
		/* If the piece is attempting to move 2 in any direction, 
		 * there must be a piece in the spot adjacent to them,
		 * in the direction they are trying to jump. */
		if (Math.abs(dest.x - start.x) == 2)
			signNumX = Integer.signum(dest.x - start.x);
		else
			signNumX = 0;
		
		if (Math.abs(dest.y - start.y) == 2)
			signNumY = Integer.signum(dest.y - start.y);
		else
			signNumY = 0;
		
		if (board[start.x + signNumX][start.y + signNumY] < 0)
			return false;
		
		/* If moving a single space... */
		if (signNumX == 0 && signNumY == 0)
		{
			/* If a jump has already been made, then can't move single spaces */
			if (this.jumpMade)
				throw new IllegalMoveException("Piece must jump another piece to continue"
						+ "moving.\n",
					start, dest);
		}
		else
		{
			this.jumpMade = true;
		}
		
		return true;
	}
	
	/**
	 * Returns true if the given player has won, false otherwise
	 * 
	 * @param player A player
	 * @return True if the given player has won, false otherwise
	 */
	public boolean playerHasWon(Player player)
	{
		if (player == Player.ONE)
		{
			for (int col = 0; col < NUM_COLS; col++)
			{
				if (board[NUM_ROWS - 1][col] < 0 ||
					board[NUM_ROWS - 1][col] > 7)
				return false;
			}
		}
		else /* Player two */
		{
			for (int col = 0; col < NUM_COLS; col++)
			{
				if (board[0][col] < 8 ||
					board[0][col] > 15)
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Shuffle the pieces in the players' staring positions
	 */
	private void shuffleStartingPos()
	{
		int index, temp;
		
		Random rnd = new Random();
		/* Shuffle player 1 row */
	    for (int i = NUM_COLS - 1; i > 0; i--)
	    {
	      index = rnd.nextInt(i+1);
	      // Swap
	      temp = board[0][index];
	      board[0][index] = board[0][i];
	      board[0][i] = temp;
	    }
	    /* Shuffle player 2 row */
	    for (int i = NUM_COLS - 1; i > 0; i--)
	    {
	      index = rnd.nextInt(i+1);
	      // Swap
	      temp = board[NUM_ROWS - 1][index];
	      board[NUM_ROWS - 1][index] = board[NUM_ROWS-1][i];
	      board[NUM_ROWS - 1][i] = temp;
	    }
	}
}

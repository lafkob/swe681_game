package edu.swe681.traverse.model;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An immutable Traverse game board
 * 
 * Variations in our rules from typical Traverse rules:
 * - If the other player's starting row is empty of *their* pieces and there
 * is a piece in the current player's row that can move, the player
 * must move a piece from their row.
 * - A player may not backtrack to the same destination (with the same piece) on their next turn 
 */
public final class GameBoard
{
	/* The size of the board. Note that if we were to add
	 * two other players, we'd have to do quite a bit of adjusting
	 * to the checks. */
	private static final int SIZE = 10;
	private static final int EMPTY = -1;
	private static final int STARTING_ROW_P1 = 0;
	private static final int STARTING_ROW_P2 = SIZE - 1;
	/* Note: Though these players are not in use, their starting lines are on the
	 * board, and restricted areas of movement. */
	private static final int STARTING_COL_P3 = 0;
	private static final int STARTING_COL_P4 = SIZE - 1;
	
	private int board[][];
	private GameState currentState;
	/* Tracks when jumps have been made in a series of moves */
	private boolean jumpMade;
	
	/* Player blocking protection variables. This is super messy though..hmm
	 * I've considered making a move object, but not sure it's worth it just for this. */
	private Point oneDestAgoP1, twoDestAgoP1;
	private Point oneDestAgoP2, twoDestAgoP2;
	private int oneIDAgoP1, twoIDAgoP1;
	private int oneIDAgoP2, twoIDAgoP2;
	
	/**
	 * Default constructor. The game board begins with all
	 * pieces in their starting lines. If shuffle is true,
	 * the starting locations will be randomly shuffled.
	 * 
	 * @param shuffle Shuffles the starting locations if true,
	 * 	does not otherwise
	 */
	public GameBoard(boolean shuffle)
	{
		board = new int[SIZE][SIZE];
		for (int row = 0; row < SIZE; row++)
		{
			for (int col = 0; col < SIZE; col++)
			{
				/* This prevents us from putting pieces in the
				 * corners */
				if (col == STARTING_COL_P3 || col == STARTING_COL_P4)
					board[row][col] = EMPTY;
				else
				{
					/* Player 1 starting line */
					if (row == STARTING_ROW_P1)
						board[row][col] = col-1;
					/* Player 2 starting line */
					else if (row == STARTING_ROW_P2)
						board[row][col] = col+SIZE-3;
					else
						board[row][col] = EMPTY;
				}
			}
		}
		
		/* Shuffle the starting positions for players 1 and 2 */
		if (shuffle)
			shuffleStartingPos();
		
		this.currentState = new GameState(GameStatus.PLAY, Player.ONE);
		
		this.oneDestAgoP1 = this.twoDestAgoP1 = null;
		this.oneIDAgoP1 = twoIDAgoP1 = -1;
		
		this.oneDestAgoP2 = this.twoDestAgoP2 = null;
		this.oneIDAgoP2 = twoIDAgoP2 = -1;
	}
	
	/**
	 * An internal copy constructor
	 * 
	 * @param oldBoard GameBoard to copy to the new one
	 */
	protected GameBoard(GameBoard oldBoard)
	{
		int[][] newBoard;
		
		newBoard = new int[SIZE][SIZE];
		for (int row = 0; row < SIZE; row++)
		{
			for (int col = 0; col < SIZE; col++)
			{
				newBoard[row][col] = oldBoard.board[row][col];
			}
		}
		this.board = newBoard;
		
		this.currentState = new GameState(
				oldBoard.getCurrentState().getStatus(),
				oldBoard.getCurrentState().getPlayer());
				
		this.jumpMade = oldBoard.jumpMade;
		
		this.oneDestAgoP1 = oldBoard.oneDestAgoP1;
		this.twoDestAgoP1 = oldBoard.twoDestAgoP1;
		this.oneIDAgoP1 = oldBoard.oneIDAgoP1;
		this.twoIDAgoP1 = oldBoard.twoIDAgoP1;
		
		this.oneDestAgoP2 = oldBoard.oneDestAgoP2;
		this.twoDestAgoP2 = oldBoard.twoDestAgoP2;
		this.oneIDAgoP2 = oldBoard.oneIDAgoP2;
		this.twoIDAgoP2 = oldBoard.twoIDAgoP2;
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
	 * Returns the current location of the given piece on the board
	 * 
	 * @param pieceID A piece identifier
	 * @return The location of the piece on the board
	 */
	public Point getPieceLocation(int pieceID)
	{
		if (!(pieceID >= 0 || pieceID < Game.NUM_PIECES))
		{
			throw new IllegalArgumentException("PieceID must be between 1 and 15.");
		}
		
		for (int row = 0; row < SIZE; row++)
		{
			for (int col = 0; col < SIZE; col++)
			{
				if (board[row][col] == pieceID)
					return new Point(row, col);
			}
		}
		
		return null;
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
		
		/* Make sure the initial conditions valid before evaluating
		 * the individual moves. */
		newBoard.validateGeneralConditions(pieceID, start, dests);
		
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
				
				newBoard.updateDestinationHistory(pieceID, dests);
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
		/* I didn't really want to repeat these, but I wanted the out of bounds messages
		 * to be consistent, which required the destination to be present, which in turn
		 * required me to check if they were empty. */
		if (dests.isEmpty())
		{
			throw new InvalidMoveException("Moves are incomplete. No destinations provided.");
		}
		
		if (!(start.x >= 0 && start.x < SIZE &&
			  start.y >= 0 && start.y < SIZE))
		{
			throw new InvalidMoveException(String.format("The location [%d,%d] is not "
					+ "on the board.", start.x, start.y), start, dests.get(0));
		}
			
		return this.movePiece(board[start.x][start.y], start, dests);
	}
	
	/**
	 * Validate all conditions that can be validated without iterating through the
	 * individual moves.
	 * 
	 * @param pieceID Piece to be moved
	 * @param start Piece's starting location
	 * @param finalDest The Piece's final destination
	 * 
	 * @throws InvalidMoveException If the piece or starting location is invalid
	 * @throws IllegalMoveException If the piece or starting location is illegal
	 */
	private void validateGeneralConditions(int pieceID, Point start, List<Point> dests)
		throws InvalidMoveException, IllegalMoveException
	{			
		/* The destinations are empty. I assume this should be weeded out in the filtering outside,
		 * but just in case... */
		if (dests.isEmpty())
		{
			throw new InvalidMoveException("Moves are incomplete. No destinations provided.");
		}
		
		/* Start is out of bounds */
		if (!(start.x >= 0 && start.x < SIZE &&
			  start.y >= 0 && start.y < SIZE))
		{
			throw new InvalidMoveException(String.format("The location [%d,%d] is not "
					+ "on the board.", start.x, start.y), start, dests.get(0));
		}
		
		/* If the piece ID is not 0-NUM_PIECES(15), then something has gone wrong or there is no piece at
		 * that location. */
		if (!(pieceID >= 0 && pieceID < Game.NUM_PIECES))
		{
			if (pieceID == EMPTY)
				throw new InvalidMoveException(String.format("Invalid move. The location"
						+ " [%d,%d] is empty.", start.x, start.y));
			else
				throw new InvalidMoveException(String.format("The given piece ID (%d) is "
						+ "invalid.", pieceID));
		}
		
		/* Piece is not where the player expects it to be */
		if (board[start.x][start.y] != pieceID)
		{
			throw new InvalidMoveException(String.format("The selected piece and"
					+ " the starting location [%d,%d] do not match.", start.x, start.y));
		}
		
		/* That is not the player's piece */
		GamePiece piece = Game.PIECES[pieceID];
		if (piece.getPlayer() != currentState.getPlayer())
		{
			throw new IllegalMoveException(String.format("Illegal piece selection "
					+ "at location [%d,%d]. That is PLAYER %s\'s piece.", 
					start.x, start.y, piece.getPlayer()));
		}
		
		/* A pieces's final destination cannot be within any starting area unless it
		 * is theirs and they started there, or it is also the player's goal line. 
		 * One can never end in the corners. */
		if (violatesEndInStartingLineRule(start, dests))
		{
			throw new IllegalMoveException("Illegal series of moves. A piece cannot end in a "
					+ "starting line unless it is your starting line and you began your "
					+ "move there or it is your goal line.");
		}
		
		/* If the other player has an empty starting line and the current player does not,
		 * but they're *not* moving a piece from their starting line, then they must move 
		 * one from the starting line, if possible. */
		if (violatesOpponentEmptyStartingLineRule(start))
		{
			throw new IllegalMoveException("If your opponent's row is empty, "
					+ "yours is not, and any of those pieces can move, you "
					+ "must move them first.");
		}
		
		/* A player is not allowed to end on the same square (with the same piece) they were
		 * in two moves ago, to prevent a player from continuously repeating a move to block
		 * their opponent indefinitely. The exception is if it's the last move they can make. */
		if (violatesBacktrackRule(pieceID, dests))
		{
			throw new IllegalMoveException(String.format("Illegal move to final destination [%d,%d] "
					+ "with the current piece. You may not backtrack to the same location with "
					+ "the same piece unless it is your last possible move.", 
					dests.get(dests.size()-1).x, dests.get(dests.size()-1).y));
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
			row = SIZE - 1;
		
		if (start.x != row || dest.x != row)
		{
			throw new IllegalMoveException("Illegal move. You may only move within your "
					+ "starting line.");
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
		 * based on how this is used, we could probably make it a precondition that start
		 * is in bounds, since it's a private class. That is - this should be unreachable,
		 * but could serve as a sanity check. */
		if (!(start.x >= 0 && start.x < SIZE &&
			  start.y >= 0 && start.y < SIZE))
		{
			throw new InvalidMoveException(String.format("The location [%d,%d] is not "
					+ "on the board.", start.x, start.y), start, dest);
		}
		if (!(dest.x >= 0 && dest.x < SIZE &&
			  dest.y >= 0 && dest.y < SIZE))
		{
			throw new InvalidMoveException(String.format("The location [%d,%d] is not "
					+ "on the board.", dest.x, dest.y), start, dest);
		}
		
		/* Probably shouldn't just move the piece to the same location */
		if (start.equals(dest))
		{
			throw new IllegalMoveException("The piece has not been moved.", start, dest);
		}
		
		/* Another piece is already at the destination */
		if (board[dest.x][dest.y] >= 0)
		{
			throw new IllegalMoveException("There is already a piece at that location.",
					start, dest);
		}
		
		piece = Game.PIECES[pieceID];
		
		/* Check to make sure they're moving only one space or jumping a piece. */
		if (!isMovingLegalDistance(start, dest))
		{
			throw new IllegalMoveException("Pieces may only move one space or jump "
					+ "over another piece.", start, dest);
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
						+ "horizontally.", start, dest);
			}
		}
		
		/* Diamonds can move only diagonally */
		else if (piece.getPieceType() == GamePieceType.DIAMOND)
		{
			/* Valid direction for a diamond? */
			if (!(Math.abs(dest.x - start.x) == Math.abs(dest.y - start.y)))
			{
				throw new IllegalMoveException("A diamond may only move diagonally.",
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
						+ "diagonally or straight backwards.", start, dest);
			}
		}
		
		/* Otherwise, the piece is a circle, and circles can move in any direction. */
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
				throw new IllegalMoveException("Piece must jump another piece to continue "
						+ "moving.",
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
			for (int col = 1; col < SIZE-1; col++)
			{
				if (board[STARTING_ROW_P2][col] < 0 ||
					board[STARTING_ROW_P2][col] > 7)
				return false;
			}
		}
		else /* Player two */
		{
			for (int col = 1; col < SIZE-1; col++)
			{
				if (board[STARTING_ROW_P1][col] < 8 ||
					board[0][STARTING_ROW_P1] > 15)
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
	    for (int i = SIZE - 2; i > 1; i--)
	    {
	      index = rnd.nextInt(i)+1;
	      // Swap
	      temp = board[0][index];
	      board[0][index] = board[0][i];
	      board[0][i] = temp;
	    }
	    /* Shuffle player 2 row */
	    for (int i = SIZE - 2; i > 0; i--)
	    {
	      index = rnd.nextInt(i)+1;
	      // Swap
	      temp = board[SIZE - 1][index];
	      board[SIZE - 1][index] = board[SIZE-1][i];
	      board[SIZE - 1][i] = temp;
	    }
	}
	
	/**
	 * Returns true if moves are available to the current player, false otherwise.
	 * 
	 * @return True if moves are available, false otherwise.
	 */
	public boolean movesAreAvailable()
	{
		return otherMovesAreAvailable(-1);
	}
	
	/**
	 * Returns true if moves, excluding the given piece, are available to the current
	 * player, false otherwise. 
	 * 
	 * @param excludedPieceID Piece that will not be checked for available moves
	 * @return True if moves are available, false otherwise.
	 */
	private boolean otherMovesAreAvailable(int excludedPieceID)
	{
		if (currentState.getPlayer() == Player.ONE)
		{
			for (int i = 0; i < 8; i++)
			{
				if (i != excludedPieceID && pieceCanMove(i))
					return true;
			}
			return false;
		}
		else
		{
			for (int i = 8; i < 16; i++)
			{
				if (i != excludedPieceID && pieceCanMove(i))
					return true;
			}
			return false;
		}
	}
	
	/**
	 * Returns true if the piece can make a move, false otherwise.
	 * Note: This is an expensive operation. Use *only* in absolutely
	 * necessary situations (i.e. if the player is requesting to skip
	 * his turn). There very likely a better way, but I'm not going
	 * to look for it at the moment. 
	 *
	 * @param pieceID Identifies a piece
	 * @return A list of legal moves for the piece
	 */
	public boolean pieceCanMove(int pieceID)
	{
		List<Point> dests;
		Point start, dest;
		boolean jumpMadeStore;
		
		/* Save and remove the jump attribute so it doesn't affect the check */
		jumpMadeStore = this.jumpMade;
		this.jumpMade = false;
		
		
		/* There are two levels of distances - those to examine
		 * possible moves in the ring just around the piece,
		 * and those to examine the moves in the second ring,
		 * to check if the piece can jump anything. */
		int[][] distances = {{-1, 0, 1},
							{-2, 0, 2}};
		
		start = getPieceLocation(pieceID);
		
		/* Test every possible combination of distances. If
		 * a good move is found, return true. */
		for (int level = 0; level < distances.length; level++)
		{
			for (int x = 0; x < distances[0].length; x++)
			{
				for (int y = 0; y < distances[0].length; y++)
				{
					/* The distance (0,0) is the point itself, which
					 * we don't need to examine. */
					if (!(x==1 && y==1))
					{
						dest = new Point(start.x + distances[level][x],
								start.y + distances[level][y]);
						dests = new ArrayList<Point>();
						dests.add(dest);
						try
						{
							validateGeneralConditions(pieceID, start, dests);
							validatePlayMove(pieceID, start, dest);
							/* If it passes these two without exception, it
							 * is a legal move. */
							this.jumpMade = jumpMadeStore;
							return true;
						}
						/* I know it's generally bad practice to not check an exception,
						 * but for this purpose, we only need to know if it doesn't throw
						 * one of the exceptions. */
						catch (IllegalMoveException | InvalidMoveException ime){}
					}
				}
			}
		}
		
		this.jumpMade = jumpMadeStore;
		return false;
	}
	
	/**
	 * Returns a list of the pieces owned by the given player in that player's
	 * starting line.
	 * 
	 * @param player A player
	 * @return A list of the player's pieces in their starting line
	 */
	private List<Integer> getPiecesInStartingLine(Player player)
	{
		List<Integer> piecesInLine = new ArrayList<Integer>();
		int pieceID;
		
		for (int col = 1; col < SIZE-1; col++)
		{
			if (player == Player.ONE)
			{
				pieceID = board[STARTING_ROW_P1][col];
				if (pieceID >= 0 && pieceID < 8)
				{
					piecesInLine.add(pieceID);
				}
			}
			else /* Player TWO */
			{
				pieceID = board[STARTING_ROW_P2][col];
				if (pieceID >= 8 && pieceID < 16)
				{
					piecesInLine.add(pieceID);
				}
			}
		}
		
		return piecesInLine;
	}
	
	/**
	 * MIGHT NOT BE WORTH IT to have a second function we can just
	 * check the other function for an empty list...
	 * 
	 * @param player
	 * @return
	 */
	private boolean hasEmptyStartingLine(Player player)
	{
		int pieceID;
		
		for (int col = 1; col < SIZE-1; col++)
		{
			if (player == Player.ONE)
			{
				pieceID = board[STARTING_ROW_P1][col];
				if (pieceID >= 0 && pieceID < 8)
				{
					return false;
				}
			}
			else /* Player TWO */
			{
				pieceID = board[STARTING_ROW_P2][col];
				if (pieceID >= 8 && pieceID < 16)
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Returns true if the rules regarding ending starting lines are violated, 
	 * false otherwise.
	 * The rule:  A player may only end in a starting line if they also started
	 * in that starting line, or if that line is their goal line.
	 * 
	 * @param start Piece starting location
	 * @param dests Piece destination
	 * @return True if the rules are violated, false otherwise
	 */
	private boolean violatesEndInStartingLineRule(Point start, List<Point> dests)
	{
		Point finalDest;
		boolean endingInStartingLineWhenNotStartedThere, inGoalLine, inCorner;
		
		finalDest = dests.get(dests.size() - 1);
		
		/* Hah, needs a shorter name. I made these because otherwise the 
		 * if statements are crazy. */
		endingInStartingLineWhenNotStartedThere =
				(finalDest.x == STARTING_ROW_P1 && start.x != STARTING_ROW_P1) ||
				(finalDest.x == STARTING_ROW_P2 && start.x != STARTING_ROW_P2) ||
				(finalDest.y == STARTING_COL_P3 && start.y != STARTING_COL_P3) ||
				(finalDest.y == STARTING_COL_P4 && start.y != STARTING_COL_P4);
		inGoalLine =
				(currentState.getPlayer() == Player.ONE && finalDest.x == STARTING_ROW_P2) ||
				(currentState.getPlayer() == Player.TWO && finalDest.x == STARTING_ROW_P1);
		inCorner =
				(finalDest.x == 0 || finalDest.x == SIZE - 1) &&
				(finalDest.y == 0 || finalDest.y == SIZE - 1);
		if ((endingInStartingLineWhenNotStartedThere && !inGoalLine) || inCorner)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the empty starting line rule is violated, false otherwise.
	 * Empty starting line rule: If the opponent player has an empty starting line,
	 * but the current player does not, the current player *must* move one of their
	 * own starting line, unless all said pieces are trapped.
	 * 
	 * @param start Starting point for the move
	 * 
	 * @return True if the rule is being violated, false otherwise
	 */
	private boolean violatesOpponentEmptyStartingLineRule(Point start)
	{
		List<Integer> startingRowVals;
		Player currentPlayer, opponent;
		int startingRow;
		
		if (currentState.getPlayer() == Player.ONE)
		{
			currentPlayer = Player.ONE;
			opponent = Player.TWO;
			startingRow = STARTING_ROW_P1;
		}
		else
		{
			currentPlayer = Player.TWO;
			opponent = Player.ONE;
			startingRow = STARTING_ROW_P2;
		}
		
		if (hasEmptyStartingLine(opponent) && start.x != startingRow &&
				!hasEmptyStartingLine(currentPlayer))
		{
			startingRowVals = this.getPiecesInStartingLine(currentPlayer);
			for (Integer pid : startingRowVals)
			{
				if (pieceCanMove(pid))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Updates the destination history values following a turn
	 * 
	 * @param dests Destinations in the move
	 */
	private void updateDestinationHistory(int pieceID, List<Point> dests)
	{
		if (this.currentState.getPlayer() == Player.ONE)
		{
			twoDestAgoP1 = oneDestAgoP1;
			twoIDAgoP1 = oneIDAgoP1;
			oneDestAgoP1 = dests.get(dests.size() - 1);
			oneIDAgoP1 = pieceID;
		}
		else /* Player TWO */
		{
			twoDestAgoP2 = oneDestAgoP2;
			twoIDAgoP2 = oneIDAgoP2;
			oneDestAgoP2 = dests.get(dests.size() - 1);
			oneIDAgoP2 = pieceID;
		}
	}
	
	/**
	 * Returns true if the backtrack rule is violated, false otherwise.
	 * The rule: You may only backtrack the same piece to the same square if
	 * it is your last possible move.
	 * 
	 * @param pieceID Current piece identifier
	 * @param dests Move destinations
	 * @return True if the rule is violated, false otherwise
	 */
	private boolean violatesBacktrackRule(int pieceID, List<Point> dests)
	{
		Point destToTest;
		int idToTest;
		if (currentState.getPlayer() == Player.ONE)
		{
			destToTest = twoDestAgoP1;
			idToTest = twoIDAgoP1;
		}
		else /* Player TWO */
		{
			destToTest = twoDestAgoP2;
			idToTest = twoIDAgoP2;
		}
		
		if (destToTest != null && destToTest.equals(dests.get(dests.size()-1)) &&
			idToTest != -1 && idToTest == pieceID &&
			otherMovesAreAvailable(pieceID))
		{
			return true;
		}
		
		return false;
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
		
		for (int row = 0; row < SIZE; row++)
		{
			for (int col = 0; col < SIZE; col++)
			{
				id = board[row][col];
				if (id < 0)
				{
					if (showID || !showSymbol)
						ret += " ";
					
					if (row == STARTING_ROW_P1 || row == STARTING_ROW_P2 ||
						col == STARTING_COL_P3 || col == STARTING_COL_P4)
						ret += "=";
					else
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
				
				if (col != SIZE - 1)
					ret += " ";
			}
			ret += "\n";
		}
		
		return ret;
	}
}

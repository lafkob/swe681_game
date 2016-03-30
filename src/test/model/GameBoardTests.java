package model;
import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.swe681.traverse.model.GameBoard;
import edu.swe681.traverse.model.IllegalMoveException;
import edu.swe681.traverse.model.InvalidMoveException;

/**
 * 
 * Tests to add: starting line rule, backtrack rule
 *
 */
public class GameBoardTests
{
	GameBoard board;
	Point start;
	List<Point> dests;
	String message, expectedMessage;
	
	@Before
	public void setUp()
    {
		board = new GameBoard(false);
		message = "";
	}

	@After
	public void tearDown()
	{
		board = null;
		start = null;
		dests = null;
		message = "";
		expectedMessage = "";
	}
	
	@Test
	public void StartOutOfBoundsTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "-1,0 10,1 9");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "Move from [0,10] to [1,9] is invalid. The "
				+ "location [0,10] is not on the board.";
		assertEquals(expectedMessage, message);
	}
	
	@Test
	public void InvalidPieceSelectedTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "16,0 1,1 1");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "The given piece ID (16) is invalid.";
		assertEquals(expectedMessage, message);
	}
	
	@Test
	public void EmptyPieceSelectedTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "-1,0 1,1 1");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "Invalid move. The location [0,1] is empty.";
		assertEquals(expectedMessage, message);
	}
	
	@Test
	public void PieceAndLocationNoMatchTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "3,0 1,1 1");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "The selected piece and the starting location [0,1] do not match.";
		assertEquals(expectedMessage, message);
	}
	
	@Test
	public void NotPlayerPieceTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "8,9 1,8 1");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		
		expectedMessage = "Illegal piece selection at location [9,1]. That is PLAYER TWO's piece.";
		assertEquals(expectedMessage, message);
	}
	
	@Test
	public void EmptyDestTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "0,0 1,");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "Moves are incomplete. No destinations provided.";
		assertEquals(expectedMessage, message);
	}
	
	@Test
	public void DestOutOfBoundsTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "7,0 8,-1 8");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "Move from [0,8] to [-1,8] is invalid. "
				+ "The location [-1,8] is not on the board.";
		assertEquals(expectedMessage, message);
	}
	
	@Test
	public void PieceDidNotMoveTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "7,0 8,0 8");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "Move from [0,8] to [0,8] is illegal. The piece has not been moved.";
		assertEquals(expectedMessage, message);
	}
	
	@Test
	public void PieceAtDestinationTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "0,0 1,0 2");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "Move from [0,1] to [0,2] is illegal. There is already "
				+ "a piece at that location.";
		assertEquals(expectedMessage, message);
	}
	
	@Test
	public void MoveOverTwoSpacesTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "0,0 1,3 1");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "Move from [0,1] to [3,1] is illegal. Pieces may "
				+ "only move one space or jump over another piece.";
		assertEquals(expectedMessage, message);
	}
	
	@Test
	public void MoveTwoSpacesWithoutJumpingTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "4,0 5,2 5");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "Move from [0,5] to [2,5] is illegal. Pieces may "
				+ "only move one space or jump over another piece.";
		assertEquals(expectedMessage, message);
	}
	
	@Test
	public void MoveTwiceWithoutJumpTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "1,0 2,1 2 2 2");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "Move from [1,2] to [2,2] is illegal. You can only move "
				+ "additional spaces if the last move was a jump.";
		assertEquals(expectedMessage, message);
	}
	
	@Test
	public void JumpThenMoveOneSpaceTest()
	{
		try
		{
			board = parseAndMakeMove(board, "1,0 2,1 2");
			
			board = parseAndMakeMove(board, "0,0 1,2 3 2 4");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "Move from [2,3] to [2,4] is illegal. Piece "
				+ "must jump another piece to continue moving.";
		assertEquals(expectedMessage, message);
	}
	
	@Test
	public void IllegalSquareMoveTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "4,0 5,1 4");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "Move from [0,5] to [1,4] is illegal. A "
				+ "square may only move verically or horizontally.";
		assertEquals(expectedMessage, message);
	}
	
	@Test
	public void IllegalDiamondMoveTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "2,0 3,1 3");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "Move from [0,3] to [1,3] is illegal. A "
				+ "diamond may only move diagonally.";
		assertEquals(expectedMessage, message);
	}
	
	@Test
	public void IllegalTriangleMoveForwardPlayerOneTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "6,0 7,1 7");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "Move from [0,7] to [1,7] is illegal. A triangle "
				+ "may only move forward diagonally or straight backwards.";
		assertEquals(expectedMessage, message);
	}
	
	@Test
	public void IllegalTriangleMoveBackwardPlayerOneTest()
	{	
		int moveCount = 0;
		
		try
		{
			moveCount++;
			board = parseAndMakeMove(board, "6,0 7,1 6");
			
			moveCount++;
			board = parseAndMakeMove(board, "6,1 6,2 7");
			
			moveCount++;
			board = parseAndMakeMove(board, "6,2 7,1 8");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "Move from [2,7] to [1,8] is illegal. A triangle "
				+ "may only move forward diagonally or straight backwards.";
		assertEquals("IllegalMoveTriangleBackwards exception message does not match",
				message, expectedMessage);
		assertTrue("IllegalMoveTriangleBackwards move count does not match", moveCount == 3);
	}
	
	@Test
	public void IllegalTriangleMoveForwardPlayerTwoTest()
	{	
		try
		{
			board = board.advanceTurn();
			
			board = parseAndMakeMove(board, "14,9 7,8 7");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "Move from [9,7] to [8,7] is illegal. A triangle "
				+ "may only move forward diagonally or straight backwards.";
		assertEquals(expectedMessage, message);
	}
	
	@Test
	public void LegalTriangleMovePlayerOneTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "6,0 7,1 6");
			
			board = parseAndMakeMove(board, "6,1 6,2 7");
			
			board = parseAndMakeMove(board, "6,2 7,1 7");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		assertEquals("", message);
		assertEquals(new Point(1,7), board.getPieceLocation(6));
	}
	
	@Test
	public void LegalTriangleMovePlayerTwoTest()
	{	
		try
		{
			board = board.advanceTurn();
			
			board = parseAndMakeMove(board, "14,9 7,8 6");
			
			board = parseAndMakeMove(board, "14,8 6,7 7");
			
			board = parseAndMakeMove(board, "14,7 7,8 7");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		assertEquals("", message);
		assertEquals(new Point(8,7), board.getPieceLocation(14));
	}
	
	@Test
	public void LegalSquareMoveTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "4,0 5,1 5");
			
			board = parseAndMakeMove(board, "4,1 5,1 4");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		assertEquals("", message);
		assertEquals(new Point(1,4), board.getPieceLocation(4));
	}
	
	@Test
	public void LegalDiamondMoveTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "2,0 3,1 4");
			
			board = parseAndMakeMove(board, "2,1 4,2 3");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		assertEquals("", message);
		assertEquals(new Point(2,3), board.getPieceLocation(2));
	}
	
	@Test
	public void LegalCircleMoveTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "0,0 1,1 1");
			
			board = parseAndMakeMove(board, "0,1 1,2 2");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		assertEquals("", message);
		assertEquals(new Point(2,2), board.getPieceLocation(0));
	}
	
	@Test
	public void LegalSingeJumpTest()
	{	
		try
		{
			board = board.advanceTurn();
			
			/* Move piece 13, player two's rightmost square,
			 * up and over 2 so the other square, piece 12,
			 * can jump it */
			board = parseAndMakeMove(board, "13,9 6,8 6");
			
			board = parseAndMakeMove(board, "13,8 6,8 5");
			
			board = parseAndMakeMove(board, "12,9 5,7 5");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		assertEquals("", message);
		assertEquals(new Point(7,5), board.getPieceLocation(12));
	}
	
	@Test
	public void LegalMultiJumpTest()
	{	
		try
		{
			board = board.advanceTurn();
			
			/* Move piece 14, player two's left triangle,
			 * up and to the left */
			board = parseAndMakeMove(board, "14,9 7,8 6");
			
			/* Then move piece 10, player two's left diamond
			 * up and to the right twice */
			board = parseAndMakeMove(board, "10,9 3,8 4");
			
			board = parseAndMakeMove(board, "10,8 4,7 5");
			
			/* Finally, jump both pieces with piece 13, player
			 * two's right square */
			board = parseAndMakeMove(board, "13,9 6,7 6 7 4");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		assertEquals("", message);
		assertEquals(new Point(8,6), board.getPieceLocation(14));
		assertEquals(new Point(7,5), board.getPieceLocation(10));
		assertEquals(new Point(7,4), board.getPieceLocation(13));
	}
	
	@Test
	public void IllegalEndInStartingLineTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "0,0 1,1 1");
			
			board = parseAndMakeMove(board, "0,1 1,1 0");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "Illegal series of moves. A piece cannot end in a "
				+ "starting line unless it is your starting line and you began "
				+ "your move there or it is your goal line.";
		assertEquals(expectedMessage, message);
	}
	
	@Test
	public void IllegalEndInGoalLineCornerTest()
	{	
		GameBoard newBoard = null;
		try
		{
			/* Move piece 8 (player 2's first circle) out of his 
			 * starting row */
			board = board.advanceTurn();
			board = parseAndMakeMove(board, "8,9 1,8 2");
			
			/* Move piece 0 (player 1's left circle) all the way into
			 * his goal line */
			board = board.advanceTurn();
			board = parseAndMakeMove(board, "0,0 1,1 1");
			for (int i = 2; i <= 9; i++)
			{
				board = parseAndMakeMove(board, 
						String.format("0,%d 1,%d 1", i-1, i));
			}
			
			/* Try to move piece 0 into the corner */
			newBoard = parseAndMakeMove(board, "0,9 1,9 0");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "Illegal series of moves. A piece cannot end in a "
				+ "starting line unless it is your starting line and you began "
				+ "your move there or it is your goal line.";
		assertEquals(expectedMessage, message);
		assertTrue(board != null);
		assertTrue(newBoard == null);
		assertEquals(new Point(9, 1), board.getPieceLocation(0));
	}
	
	@Test
	public void LegalMoveWithinStartingLineTest()
	{	
		try
		{
			board = parseAndMakeMove(board, "1,0 2,1 2");
			
			board = parseAndMakeMove(board, "0,0 1,0 2");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		assertEquals("", message);
		assertEquals(new Point(0, 2), board.getPieceLocation(0));
	}
	
	@Test
	public void LegalJumpIntoAndOutOfStartingLine()
	{
		try
		{
			/* Piece 0 (P1 left circle) moves down one space */
			board = parseAndMakeMove(board, "0,0 1,1 1");
			
			/* Piece 3 (P1 left diamond) moves down and to the left
			 * three times */
			board = parseAndMakeMove(board, "3,0 4,1 3");
			
			board = parseAndMakeMove(board, "3,1 3,2 2");
			
			board = parseAndMakeMove(board, "3,2 2,3 1");
			
			/* Piece 1 (P1 right circle) jumps into and out of P3's (left col)
			 * starting line */
			board = parseAndMakeMove(board, "1,0 2,2 0 4 2");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		assertEquals("", message);
		assertEquals(new Point(1, 1), board.getPieceLocation(0));
		assertEquals(new Point(3, 1), board.getPieceLocation(3));
		assertEquals(new Point(4, 2), board.getPieceLocation(1));
	}
	
	@Test
	public void TrappedPieceTest()
	{
		try
		{
			/* Move piece 8 up one and to the far right */
			board = board.advanceTurn();
			board = parseAndMakeMove(board, "8,9 1,8 1");
			
			for (int i = 2; i <= 8; i++)
			{
				board = parseAndMakeMove(board,
						String.format("8,8 %d,8 %d", i-1, i));
			}
			
			/* Move piece 12 up two */
			board = parseAndMakeMove(board, "12,9 5,8 5");
			
			board = parseAndMakeMove(board, "12,8 5,7 5");
			
			/* Move piece 13 up one */
			board = parseAndMakeMove(board, "13,9 6,8 6");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		assertEquals("", message);
		
		/* Piece 14 should now be stuck. */
		assertTrue("Piece 14 should not be able to move.", !board.pieceCanMove(14));
		/* Piece 13, on the other hand, should be able to move just fine. */
		assertTrue("Piece 13 should be able to move.", board.pieceCanMove(13));
	}
	
	@Test
	public void OtherPlayerPieceCannotMove()
	{
		assertTrue("Piece 13 should not be able to move.", !board.pieceCanMove(13));
	}
	
	@Test
	public void ViolateEmptyStartingLineRuleTest()
	{
		try
		{
			/* A series of moves to empty player one's starting line */
			board = parseAndMakeMove(board, "7,0 8,1 7");
			board = parseAndMakeMove(board, "7,1 7,2 8");
			
			board = parseAndMakeMove(board, "6,0 7,1 8");
			
			board = parseAndMakeMove(board, "5,0 6,1 6");
			
			board = parseAndMakeMove(board, "4,0 5,1 5");
			board = parseAndMakeMove(board, "4,1 5,2 5");
			
			board = parseAndMakeMove(board, "3,0 4,1 5");
			
			board = parseAndMakeMove(board, "2,0 3,1 4");
			
			board = parseAndMakeMove(board, "1,0 2,1 2");
			
			board = parseAndMakeMove(board, "0,0 1,1 1");
			
			/* Moving one out of the second player's starting line
			 * should be fine...*/
			board = board.advanceTurn();
			board = parseAndMakeMove(board, "8,9 1,8 1");
			
			/* But we should get the error if we try to move the same piece */
			board = parseAndMakeMove(board, "8,8 1,7 1");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		expectedMessage = "If your opponent's row is empty, yours is not, and "
				+ "any of those pieces can move, you must move them first.";
		assertEquals(expectedMessage, message);
		assertEquals(new Point(8,1), board.getPieceLocation(8));
	}
	
	@Test
	public void EmptyStartingRowWithTrappedPiece()
	{
		try
		{
			/* A series of moves to all but empty player one's starting
			 * line */
			
			board = parseAndMakeMove(board, "7,0 8,1 7");
			board = parseAndMakeMove(board, "7,1 7,2 8");
			
			board = parseAndMakeMove(board, "6,0 7,1 8");
			
			board = parseAndMakeMove(board, "5,0 6,1 6");
			
			board = parseAndMakeMove(board, "4,0 5,1 5");
			board = parseAndMakeMove(board, "4,1 5,2 5");
			
			board = parseAndMakeMove(board, "3,0 4,1 5");
			
			board = parseAndMakeMove(board, "2,0 3,1 4");
			
			board = parseAndMakeMove(board, "1,0 2,1 2");
			
			//board = parseAndMakeMove(board, "0,0 1,1 1");
			/*
			= O = = = = = = = =
			= - O - + + # - ^ =
			= - - - - # - - ^ =
			= - - - - - - - - =
			= - - - - - - - - =
			= - - - - - - - - =
			= - - - - - - - - =
			= - - - - - - - - =
			= - - - - - - - - =
			= O O + + # # ^ ^ =
			 */
			
			/* Trap piece 14 */
			
			/* Move piece 8 up one and to the far right */
			board = board.advanceTurn();
			board = parseAndMakeMove(board, "8,9 1,8 1");
			
			for (int i = 2; i <= 8; i++)
			{
				board = parseAndMakeMove(board,
						String.format("8,8 %d,8 %d", i-1, i));
			}
			
			/* Move piece 12 up two */
			board = parseAndMakeMove(board, "12,9 5,8 5");
			board = parseAndMakeMove(board, "12,8 5,7 5");
			
			/* Move piece 13 up one */
			board = parseAndMakeMove(board, "13,9 6,8 6");
			
			/*
			= O = = = = = = = =
			= - O - + + # - ^ =
			= - - - - # - - ^ =
			= - - - - - - - - =
			= - - - - - - - - =
			= - - - - - - - - =
			= - - - - - - - - =
			= - - - - # - - - =
			= - - - - - # - O =
			= = O + + = = ^ ^ =
			*/
			
			/* A series of moves to finish emptying player two's starting line. */
			board = parseAndMakeMove(board, "9,9 2,8 2");
			
			board = parseAndMakeMove(board, "10,9 3,8 4");
			
			board = parseAndMakeMove(board, "11,9 4,8 5");
			
			board = parseAndMakeMove(board, "15,9 8,8 7");
			
			/*
			= O = = = = = = = =
			= - O - + + # - ^ =
			= - - - - # - - ^ =
			= - - - - - - - - =
			= - - - - - - - - =
			= - - - - - - - - =
			= - - - - - - - - =
			= - - - - # - - - =
			= - O - + + # ^ O =
			= = = = = = = ^ = =
			*/
			
			/* Move the last piece out of player 1's row...*/
			board = board.advanceTurn();
			board = parseAndMakeMove(board, "0,0 1,1 1");
			
			/* Now, despite the fact that player two has a piece in his
			 * row, it's *trapped*, so we should be able to move another of his
			 * pieces...*/
			board = board.advanceTurn();
			board = parseAndMakeMove(board, "12,7 5,6 5");
			/*
			= = = = = = = = = =
			= O O - + + # - ^ =
			= - - - - # - - ^ =
			= - - - - - - - - =
			= - - - - - - - - =
			= - - - - - - - - =
			= - - - - # - - - =
			= - - - - - - - - =
			= - O - + + # ^ O =
			= = = = = = = ^ = =
			 */
			
			/*...but once the piece is freed by that move, he has to move it. */
			board = parseAndMakeMove(board, "12,6 5,6 4");
			
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}

		expectedMessage = "If your opponent's row is empty, yours is not, and "
				+ "any of those pieces can move, you must move them first.";
		assertEquals(expectedMessage, message);
		assertEquals(new Point(6,5), board.getPieceLocation(12));
	}
	
	@Test
	public void ViolateBacktrackRule()
	{
		try
		{
			/* Just moving a piece back and forth to see if it gets
			 * caught. */
			board = parseAndMakeMove(board, "0,0 1,1 1");
			board = parseAndMakeMove(board, "0,1 1,2 1");
			board = parseAndMakeMove(board, "0,2 1,1 1");
		}
		catch (IllegalMoveException | InvalidMoveException ime)
		{
			message = ime.getMessage();
		}
		
		expectedMessage = "Illegal move to final destination [1,1] with the "
				+ "current piece. You may not backtrack to the same location with "
				+ "the same piece unless it is your last possible move.";
		assertEquals(expectedMessage, message);
	}
	
	private GameBoard parseAndMakeMove(GameBoard board, String moveStr)
		throws IllegalMoveException, InvalidMoveException
	{
		String[] tokens, subTokens;
		int pieceID;
		Point start;
		List<Point> dests;
		
		tokens = moveStr.split(",");
		
		pieceID = Integer.parseInt(tokens[0]);
		
		subTokens = tokens[1].split(" ");
		start = new Point(Integer.parseInt(subTokens[0]), Integer.parseInt(subTokens[1]));
		
		dests = new ArrayList<Point>();
		if (tokens.length > 2)
		{
			subTokens = tokens[2].split(" ");
			for (int i = 0; i < subTokens.length; i=i+2)
			{
				dests.add(new Point(Integer.parseInt(subTokens[i]), Integer.parseInt(subTokens[i+1])));
			}
		}
		
		return board.movePiece(pieceID, start, dests);		
	}
}

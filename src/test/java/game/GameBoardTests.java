package game;
import static org.junit.Assert.*;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.swe681.traverse.game.GameBoard;
import edu.swe681.traverse.game.GameState;
import edu.swe681.traverse.game.MoveHistory;
import edu.swe681.traverse.game.enums.GameStatus;
import edu.swe681.traverse.game.exception.*;
import edu.swe681.traverse.model.GameModel;

/**
 * 
 * Tests to add: starting line rule, backtrack rule
 *
 */
public class GameBoardTests
{
	private GameBoard board;
	private int[][] boardSetup;
	private int genericMoveNum;
	private int gameID = 10;
	private Long playerOneID = 20L;
	private Long playerTwoID = 30L;
	
	@Before
	public void setUp()
    {
		board = new GameBoard(gameID, playerOneID, false);
		try
		{
			board = board.registerPlayerTwo(playerTwoID);
		} catch (TraverseException te)
		{
			System.out.println(te.getMessage());
		}
		genericMoveNum = 0;
	}

	@After
	public void tearDown()
	{
		board = null;
	}
	
	@Test
	public void GetterTest()
	{
		assertEquals("Get game ID", gameID, board.getGameID());
		assertEquals("Get player one ID", playerOneID, board.getPlayerOneID());
		assertEquals("Get player two ID", playerTwoID, board.getPlayerTwoID());
	}
	
	@Test (expected=InvalidGameInputException.class)
	public void GetInvalidPieceTest() throws TraverseException
	{
		board.getPieceLocation(-1);
	}
	
	@Test
	public void ShuffleTest()
	{
		/* Primarily this is just to make sure shuffle runs without breaking. Hard to 
		 * make a hard test for randomization...*/
		board = new GameBoard(gameID, playerOneID, true);
	}
	
	@Test
	public void CorrectGameModelTest() throws IOException
	{
		GameModel model = new GameModel(board);
		GameBoard boardFromModel = new GameBoard(model);
		
		int[][] modelBoard, returnedBoard;
		modelBoard = model.boardAsArray();
		returnedBoard = boardFromModel.getBoard();
		for (int i = 0; i < modelBoard.length; i++)
		{
			assertTrue(Arrays.equals(modelBoard[i], returnedBoard[i]));
		}
		
		assertEquals(model.getPlayerOneId(), boardFromModel.getPlayerOneID());
		assertEquals(model.getPlayerTwoId(), boardFromModel.getPlayerTwoID());
		assertEquals(model.getGameId(), boardFromModel.getGameID());
		assertEquals(new GameState(model.getGameStatus(), model.getCurrentPlayerId()),
				board.getGameState());
		assertEquals(new MoveHistory(model.getP1OneMoveAgoX(), model.getP1OneMoveAgoY(),
						model.getP1OneIdAgo(), model.getP1TwoMoveAgoX(), model.getP1TwoMoveAgoY(),
						model.getP1TwoIdAgo()),
					 boardFromModel.getMoveHistory(playerOneID));
		assertEquals(new MoveHistory(model.getP2OneMoveAgoX(), model.getP2OneMoveAgoY(),
				model.getP2OneIdAgo(), model.getP2TwoMoveAgoX(), model.getP2TwoMoveAgoY(),
				model.getP2TwoIdAgo()),
			 boardFromModel.getMoveHistory(playerTwoID));
	}
	
	@Test
	public void LegalRegisterPlayerTest() throws TraverseException
	{
		board = new GameBoard(gameID, playerOneID, false);
		board = board.registerPlayerTwo(playerTwoID);
		assertEquals(new GameState(GameStatus.PLAY, playerOneID), board.getGameState());
	}
	
	@Test (expected=InvalidGameStateException.class)
	public void RegisterInIllegalStateTest() throws TraverseException
	{
		board = board.playerQuit(playerTwoID);
		board.registerPlayerTwo(playerTwoID);
	}
	
	@Test (expected=InvalidGameInputException.class)
	public void NegativeRegisterPlayerTest() throws TraverseException
	{
		board = new GameBoard(gameID, playerOneID, false);
		board.registerPlayerTwo(-3L);
	}
	
	@Test (expected=InvalidGameStateException.class)
	public void BothPlayersAlreadyRegisteredTest() throws TraverseException
	{
		board.registerPlayerTwo(playerTwoID);
	}
	
	@Test (expected=InvalidGameStateException.class)
	public void MoveBeforeGameStartTest() throws TraverseException
	{
		board = new GameBoard(gameID, playerOneID, false);
		board = parseAndMakeMove(board, "0,1 0");
	}
	
	@Test
	public void LegalForfeitTest() throws TraverseException
	{
		board = board.playerQuit(playerTwoID);
		assertEquals(new GameState(GameStatus.FORFEIT, playerOneID),
				board.getGameState());
	}
	
	@Test (expected=InvalidGameStateException.class)
	public void IllegalForfeitStateTest() throws TraverseException
	{
		board = new GameBoard(gameID, playerOneID, false);
		board.playerQuit(playerTwoID);
	}
	
	@Test (expected=InvalidGameInputException.class)
	public void InvalidPlayerIDForfeitTest() throws TraverseException
	{
		board.playerQuit(5L);
	}
		
	@Test (expected=InvalidGameInputException.class)
	public void InvalidPieceSelectedTest() throws TraverseException
	{
		board = parseAndMakeMove(board, "16,1 1");
	}
	
	@Test (expected=IllegalPieceSelectionException.class)
	public void NotPlayerPieceTest() throws TraverseException
	{	
		board = parseAndMakeMove(board, "8,8 1");
	}
	
	@Test (expected=InvalidGameInputException.class)
	public void EmptyDestTest() throws TraverseException
	{	
		board = parseAndMakeMove(board, "0");
	}
	
	@Test (expected=InvalidGameInputException.class)
	public void DestOutOfBoundsTest() throws TraverseException
	{	
		board = parseAndMakeMove(board, "7,-1 8");
	}
	
	@Test (expected=IllegalMoveException.class)
	public void PieceDidNotMoveTest() throws TraverseException
	{	
		board = parseAndMakeMove(board, "7,0 8");
	}
	
	@Test (expected=IllegalAreaException.class)
	public void PieceAtDestinationTest() throws TraverseException
	{	
		board = parseAndMakeMove(board, "0,0 2");
	}
	
	@Test (expected=IllegalMoveException.class)
	public void MoveOverTwoSpacesTest() throws TraverseException
	{	
		board = parseAndMakeMove(board, "0,3 1");
	}
	
	@Test (expected=IllegalMoveException.class)
	public void MoveTwoSpacesWithoutJumpingTest() throws TraverseException
	{
		board = parseAndMakeMove(board, "4,2 5");
	}
	
	@Test (expected=IllegalMultiMoveException.class)
	public void MoveTwiceWithoutJumpTest() throws TraverseException
	{	
		board = parseAndMakeMove(board, "1,1 2 2 2");
	}
	
	@Test (expected=IllegalMultiMoveException.class)
	public void JumpThenMoveOneSpaceTest() throws TraverseException
	{
		boardSetup = parseGameBoardString(
				  "=  0  -  2  3  4  5  6  7 =\n"
			    + "=  -  1  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  8  9 10 11 12 13 14 15 =");
		
		board = new GameBoard(gameID, playerOneID, playerTwoID, boardSetup,
				new GameState(GameStatus.PLAY, playerOneID), new MoveHistory(),
				new MoveHistory());
		
		board = parseAndMakeMove(board, "0,2 3 2 4");
	}
	
	@Test (expected=IllegalMoveException.class)
	public void IllegalSquareMoveTest() throws TraverseException
	{	
		board = parseAndMakeMove(board, "4,1 4");
	}
	
	@Test (expected=IllegalMoveException.class)
	public void IllegalDiamondMoveTest() throws TraverseException
	{	
		board = parseAndMakeMove(board, "2,1 3");
	}
	
	@Test (expected=IllegalMoveException.class)
	public void IllegalTriangleMoveForwardPlayerOneTest() throws TraverseException
	{	
		board = parseAndMakeMove(board, "6,1 7");
	}
	
	@Test (expected=IllegalMoveException.class)
	public void IllegalTriangleMoveBackwardPlayerOneTest() throws TraverseException
	{	
		boardSetup = parseGameBoardString(
				  "=  0  1  2  3  4  5  -  7 =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  6  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  8  9 10 11 12 13 14 15 =");

		board = new GameBoard(gameID, playerOneID, playerTwoID, boardSetup,
				new GameState(GameStatus.PLAY, playerOneID), new MoveHistory(),
				new MoveHistory());
		
		board = parseAndMakeMove(board, "6,1 8");
	}
	
	@Test (expected=IllegalMoveException.class)
	public void IllegalTriangleMoveForwardPlayerTwoTest() throws TraverseException
	{	
		board = makeGenericMoveToAdvanceTurn(board);
		
		board = parseAndMakeMove(board, "14,8 7");
	}
	
	@Test
	public void LegalTriangleMovePlayerOneTest() throws TraverseException
	{	
		board = parseAndMakeMove(board, "6,1 6");
		board = makeGenericMoveToAdvanceTurn(board);
		
		board = parseAndMakeMove(board, "6,2 7");
		board = makeGenericMoveToAdvanceTurn(board);
		
		board = parseAndMakeMove(board, "6,1 7");

		assertEquals(new Point(1,7), board.getPieceLocation(6));
	}
	
	@Test
	public void LegalTriangleMovePlayerTwoTest() throws TraverseException
	{	
		board = makeGenericMoveToAdvanceTurn(board);
		
		board = parseAndMakeMove(board, "14,8 6");
		board = makeGenericMoveToAdvanceTurn(board);
		
		board = parseAndMakeMove(board, "14,7 7");
		board = makeGenericMoveToAdvanceTurn(board);
		
		board = parseAndMakeMove(board, "14,8 7");
	
		assertEquals(new Point(8,7), board.getPieceLocation(14));
	}
	
	@Test
	public void LegalSquareMoveTest() throws TraverseException
	{	
		board = parseAndMakeMove(board, "4,1 5");
		board = makeGenericMoveToAdvanceTurn(board);
		
		board = parseAndMakeMove(board, "4,1 4");

		assertEquals(new Point(1,4), board.getPieceLocation(4));
	}
	
	@Test
	public void LegalDiamondMoveTest() throws TraverseException
	{	
		board = parseAndMakeMove(board, "2,1 4");
		board = makeGenericMoveToAdvanceTurn(board);
		
		board = parseAndMakeMove(board, "2,2 3");

		assertEquals(new Point(2,3), board.getPieceLocation(2));
	}
	
	@Test
	public void LegalCircleMoveTest() throws TraverseException
	{	
		board = parseAndMakeMove(board, "0,1 1");
		board = makeGenericMoveToAdvanceTurn(board);
		
		board = parseAndMakeMove(board, "0,2 1");

		assertEquals(new Point(2,1), board.getPieceLocation(0));
	}
	
	@Test
	public void LegalSingeJumpTest() throws TraverseException
	{	
		boardSetup = parseGameBoardString(
				  "=  0  1  2  3  4  5  6  7 =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  - 13  -  -  - =\n"
			    + "=  8  9 10 11 12  - 14 15 =");

		board = new GameBoard(gameID, playerOneID, playerTwoID, boardSetup,
				new GameState(GameStatus.PLAY, playerTwoID), new MoveHistory(),
				new MoveHistory());
		
		/* Move piece 13, player two's rightmost square,
		 * up and over 2 so the other square, piece 12,
		 * can jump it */
		board = parseAndMakeMove(board, "12,7 5");

		assertEquals(new Point(7,5), board.getPieceLocation(12));
	}
	
	@Test
	public void LegalMultiJumpTest() throws TraverseException
	{	
		boardSetup = parseGameBoardString(
				  "=  0  1  2  3  4  5  6  7 =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  - 10  -  -  - =\n"
			    + "=  -  -  -  -  - 14  -  - =\n"
			    + "=  8  9  - 11 12 13  - 15 =");

		board = new GameBoard(gameID, playerOneID, playerTwoID, boardSetup,
				new GameState(GameStatus.PLAY, playerTwoID), new MoveHistory(),
				new MoveHistory());
		
		/* Jump 10 and 13 with piece 13, player
		 * two's right square */
		board = parseAndMakeMove(board, "13,7 6 7 4");

		assertEquals(new Point(7,4), board.getPieceLocation(13));
	}
	
	@Test (expected=IllegalAreaException.class)
	public void IllegalEndInStartingLineTest() throws TraverseException
	{	
		boardSetup = parseGameBoardString(
				  "=  -  1  2  3  4  5  6  7 =\n"
			    + "=  0  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  8  9 10 11 12 13 14 15 =");

		board = new GameBoard(gameID, playerOneID, playerTwoID, boardSetup,
				new GameState(GameStatus.PLAY, playerOneID), new MoveHistory(),
				new MoveHistory());
		
		board = parseAndMakeMove(board, "0,1 0");
	}
	
	@Test (expected=IllegalAreaException.class)
	public void IllegalEndInGoalLineCornerTest() throws TraverseException
	{	
		boardSetup = parseGameBoardString(
				  "=  -  1  2  3  4  5  6  7 =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  8  -  -  -  -  -  - =\n"
			    + "=  0  9 10 11 12 13 14 15 =");

		board = new GameBoard(gameID, playerOneID, playerTwoID, boardSetup,
				new GameState(GameStatus.PLAY, playerOneID), new MoveHistory(),
				new MoveHistory());
		
		/* Try to move piece 0 into the corner */
		board = parseAndMakeMove(board, "0,9 0");
	}
	
	@Test
	public void LegalMoveWithinStartingLineTest() throws TraverseException
	{	
		boardSetup = parseGameBoardString(
				  "=  0  -  2  3  4  5  6  7 =\n"
			    + "=  -  1  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  8  9 10 11 12 13 14 15 =");

		board = new GameBoard(gameID, playerOneID, playerTwoID, boardSetup,
				new GameState(GameStatus.PLAY, playerOneID), new MoveHistory(),
				new MoveHistory());
		
		board = parseAndMakeMove(board, "0,0 2");

		assertEquals(new Point(0, 2), board.getPieceLocation(0));
	}
	
	@Test
	public void LegalJumpIntoAndOutOfStartingLine() throws TraverseException
	{
		boardSetup = parseGameBoardString(
				  "=  -  1  2  -  4  5  6  7 =\n"
			    + "=  0  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  3  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  8  9 10 11 12 13 14 15 =");

		board = new GameBoard(gameID, playerOneID, playerTwoID, boardSetup,
				new GameState(GameStatus.PLAY, playerOneID), new MoveHistory(),
				new MoveHistory());

		board = parseAndMakeMove(board, "1,2 0 4 2");
		
		assertEquals(new Point(4,2), board.getPieceLocation(1));
	}
	
	@Test
	public void TrappedPieceTest() throws TraverseException
	{
		boardSetup = parseGameBoardString(
				  "=  0  1  2  3  4  5  6  7 =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  - 12  -  -  - =\n"
			    + "=  -  -  -  -  - 13  -  8 =\n"
			    + "=  -  9 10 11  -  - 14 15 =");

		board = new GameBoard(gameID, playerOneID, playerTwoID, boardSetup,
				new GameState(GameStatus.PLAY, playerTwoID), new MoveHistory(),
				new MoveHistory());

		/* Piece 14 should now be stuck. */
		assertTrue("Piece 14 should not be able to move.", !board.pieceCanMove(14));
		/* Piece 13, on the other hand, should be able to move just fine. */
		assertTrue("Piece 13 should be able to move.", board.pieceCanMove(13));
	}
	
	@Test
	public void OtherPlayerPieceCannotMove() throws TraverseException
	{
		assertTrue(!board.pieceCanMove(13));
	}
	
	@Test (expected=AlternateMoveRequiredException.class)
	public void ViolateEmptyStartingLineRuleTest() throws TraverseException
	{
		boardSetup = parseGameBoardString(
				  "=  0  -  -  -  -  -  -  - =\n"
			    + "=  -  1  -  2  3  5  -  6 =\n"
			    + "=  -  -  -  -  4  -  -  7 =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  8  9 10 11 12 13 14 15 =");

		board = new GameBoard(gameID, playerOneID, playerTwoID, boardSetup,
				new GameState(GameStatus.PLAY, playerTwoID), new MoveHistory(),
				new MoveHistory());
		
		/* Player ONE's row is now empty */

		/* Moving one out of the second player's starting line
		 * should be fine...*/
		board = parseAndMakeMove(board, "8,8 1");
		board = makeGenericMoveToAdvanceTurn(board);
		
		assertEquals(new Point(8,1), board.getPieceLocation(8));
		
		/* But we should get the error if we try to move the same piece */
		board = parseAndMakeMove(board, "8,7 1");
	}
	
	@Test (expected=AlternateMoveRequiredException.class)
	public void EmptyStartingRowWithTrappedPiece() throws TraverseException
	{
		boardSetup = parseGameBoardString(
				  "=  0  -  -  -  -  -  -  - =\n"
			    + "=  -  1  -  2  3  5  -  6 =\n"
			    + "=  -  -  -  -  4  -  -  7 =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  - 12  -  -  - =\n"
			    + "=  -  9  - 10 11 13 15  8 =\n"
			    + "=  -  -  -  -  -  - 14  - =");

		board = new GameBoard(gameID, playerOneID, playerTwoID, boardSetup,
				new GameState(GameStatus.PLAY, playerOneID), new MoveHistory(),
				new MoveHistory());
		/* Start the board with Player 1's starting row
		 * almost empty and piece 14 trapped */
		
		/* Move the last piece out of player 1's row...*/
		board = parseAndMakeMove(board, "0,1 1");
		
		/* Now, despite the fact that player two has a piece in his
		 * row, it's *trapped*, so we should be able to move another of his
		 * pieces...*/
		board = parseAndMakeMove(board, "12,6 5");
		genericMoveNum++;
		board = makeGenericMoveToAdvanceTurn(board);
		
		assertEquals(new Point(6,5), board.getPieceLocation(12));
		
		/*...but once the piece is freed by that move, he has to move it. */
		board = parseAndMakeMove(board, "12,6 4");
	}
	
	@Test (expected=AlternateMoveRequiredException.class)
	public void ViolateBacktrackRule() throws TraverseException
	{

		/* Just moving a piece back and forth to see if it gets
		 * caught. */
		board = parseAndMakeMove(board, "0,1 1");
		board = makeGenericMoveToAdvanceTurn(board);
		
		board = parseAndMakeMove(board, "0,2 1");
		board = makeGenericMoveToAdvanceTurn(board);
		
		assertEquals(new Point(2,1), board.getPieceLocation(0));
		
		board = parseAndMakeMove(board, "0,1 1");
	}
	
	@Test (expected=InvalidGameStateException.class)
	public void PlayerHasWonTest() throws TraverseException
	{
		boardSetup = parseGameBoardString(
				  "=  8  9 10 11  - 13 14 15 =\n"
			    + "=  -  -  -  - 12  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  -  -  -  -  -  -  - =\n"
			    + "=  -  1  -  -  -  -  -  - =\n"
			    + "=  0  -  2  3  4  5  6  7 =");

		board = new GameBoard(gameID, playerOneID, playerTwoID, boardSetup,
				new GameState(GameStatus.PLAY, playerOneID), new MoveHistory(),
				new MoveHistory());
		
		assertTrue("Player one should not have won yet.", !board.playerHasWon(playerOneID));
		assertTrue("Player two should not have won yet.", !board.playerHasWon(playerTwoID));

		board = parseAndMakeMove(board, "1,9 2");
		assertTrue("Player one should have won.", board.playerHasWon(playerOneID));
		assertEquals("Game state should be WIN with player one's ID.",
				new GameState(GameStatus.WIN, playerOneID), board.getGameState());
		
		board = parseAndMakeMove(board, "12,0 5");
	}
	
	private GameBoard parseAndMakeMove(GameBoard board, String moveStr)
		throws TraverseException
	{
		String[] tokens, subTokens;
		int pieceID;
		List<Point> dests;
		
		tokens = moveStr.split(",");
		
		pieceID = Integer.parseInt(tokens[0]);
		
		dests = new ArrayList<Point>();
		if (tokens.length > 1)
		{
			subTokens = tokens[1].split(" ");
			for (int i = 0; i < subTokens.length; i=i+2)
			{
				dests.add(new Point(Integer.parseInt(subTokens[i]), Integer.parseInt(subTokens[i+1])));
			}
		}
		
		return board.movePiece(pieceID, dests);		
	}
	
	private int[][] parseGameBoardString(String board)
	{
		String[] rows;
		String[] cols;
		int[][] boardSetup;
		
		boardSetup = new int[GameBoard.SIZE][GameBoard.SIZE];

		rows = board.split("\\n");
		for (int row = 0; row < GameBoard.SIZE; row++)
		{
			cols = rows[row].split("[ ]+");
			for (int col = 0; col < GameBoard.SIZE; col++)
			{
				if (cols[col].equals("=") || cols[col].equals("-"))
				{
					boardSetup[row][col] = -1;
				}
				else
					boardSetup[row][col] = Integer.parseInt(cols[col]);
			}
		}

		return boardSetup;
	}
	
	/**
	 * A testing class that makes a generic move with the given player, just
	 * to advance the turn. May only be called eight times and moves piece 0
	 * vertically down the board - easy to break, so be careful
	 * 
	 * @param board GameBoard to affect
	 * 
	 * @return Board with the generic move made
	 */
	private GameBoard makeGenericMoveToAdvanceTurn(GameBoard board) throws TraverseException
	{
		GameBoard newBoard;
		
		if (board.getGameState().getCurrentPlayerID() == board.getPlayerOneID())
		{
			newBoard = parseAndMakeMove(board, String.format("0,%d 1", genericMoveNum+1));
			genericMoveNum++;
		}
		else /* Player TWO */
		{
			newBoard = parseAndMakeMove(board, String.format("8,%d 1", 9-(genericMoveNum+1)));
			genericMoveNum++;
		}
		
		return newBoard;
	}
}

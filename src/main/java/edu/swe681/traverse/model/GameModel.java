package edu.swe681.traverse.model;

import java.util.Objects;

import edu.swe681.traverse.game.GameBoard;

public class GameModel {
	/*
	GameBoard
	---------
	board: int[][]
	gameState: GameState
	gameID: int
	playerOneID: int
	playerTwoID: int
	p1History: MoveHistory
	p2History: MoveHistory
	
	GameState
	---------
	status: GameStatus (enum)
	currentPlayerID: int
	
	MoveHistory
	------------
	oneMoveAgo: Point
	twoMoveAgo: Point
	oneIDAgo: int
	twoIDAgo: int
	 */
	
	// game board
	// TODO: serialize 2d int array
	private long gameId;
	private long playerOneId;
	private long playerTwoId;
	
	// game state
	private String gameStatus;
	private long currentPlayerId;

	// p1MoveHistory
	private int p1OneMoveAgoX;
	private int p1OneMoveAgoY;
	private int p1TwoMoveAgoX;
	private int p1TwoMoveAgoY;
	private int p1OneIdAgo;
	private int p1TwoIdAgo;
	
	// p2MoveHistory
	private int p2OneMoveAgoX;
	private int p2OneMoveAgoY;
	private int p2TwoMoveAgoX;
	private int p2TwoMoveAgoY;
	private int p2OneIdAgo;
	private int p2TwoIdAgo;
	
	public GameModel(GameBoard board){
		Objects.requireNonNull(board, "requires GameBoard");
		
		// game board
		// TODO: 2d array
		this.gameId = board.getGameID();
		this.playerOneId = board.getPlayerOneID();
		this.playerTwoId = board.getPlayerTwoID();
		
		// game state
		this.gameStatus = board.getGameState().getStatus().toString();
		this.currentPlayerId = board.getGameState().getCurrentPlayerID();
		
		// p1MoveHistory
		this.p1OneMoveAgoX = board.getMoveHistory(board.getPlayerOneID()).getOneMoveAgo().x;
		this.p1OneMoveAgoY = board.getMoveHistory(board.getPlayerOneID()).getOneMoveAgo().y;
		this.p1TwoMoveAgoX = board.getMoveHistory(board.getPlayerOneID()).getTwoMoveAgo().x;
		this.p1TwoMoveAgoY = board.getMoveHistory(board.getPlayerOneID()).getTwoMoveAgo().y;
		this.p1OneIdAgo = board.getMoveHistory(board.getPlayerOneID()).getOneIDAgo();
		this.p1TwoIdAgo = board.getMoveHistory(board.getPlayerOneID()).getTwoIDAgo();
		
		// p2MoveHistory
		this.p2OneMoveAgoX = board.getMoveHistory(board.getPlayerTwoID()).getOneMoveAgo().x;
		this.p2OneMoveAgoY = board.getMoveHistory(board.getPlayerTwoID()).getOneMoveAgo().y;
		this.p2TwoMoveAgoX = board.getMoveHistory(board.getPlayerTwoID()).getTwoMoveAgo().x;
		this.p2TwoMoveAgoY = board.getMoveHistory(board.getPlayerTwoID()).getTwoMoveAgo().y;
		this.p2OneIdAgo = board.getMoveHistory(board.getPlayerTwoID()).getOneIDAgo();
		this.p2TwoIdAgo = board.getMoveHistory(board.getPlayerTwoID()).getTwoIDAgo();
	}
}

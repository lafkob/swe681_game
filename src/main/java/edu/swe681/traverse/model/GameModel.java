package edu.swe681.traverse.model;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.swe681.traverse.game.GameBoard;

/**
 * Represents a game model in the database. Easily mapped from an
 * instance of a GameBoard.
 */
public class GameModel {
	
	// game board
	private String board; // serialized
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
	
	public GameModel(GameBoard board) throws JsonProcessingException{
		Objects.requireNonNull(board, "requires GameBoard");
		
		// game board
		this.board = serialize2dIntArray(board.getBoard());
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
	
	private String serialize2dIntArray(int[][] arr) throws JsonProcessingException{
		return new ObjectMapper().writeValueAsString(arr);
	}
	
	private int[][] deserialize2dIntArray(String json) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(json, int[][].class);
	}

	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public long getPlayerOneId() {
		return playerOneId;
	}

	public void setPlayerOneId(long playerOneId) {
		this.playerOneId = playerOneId;
	}

	public long getPlayerTwoId() {
		return playerTwoId;
	}

	public void setPlayerTwoId(long playerTwoId) {
		this.playerTwoId = playerTwoId;
	}

	public String getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public long getCurrentPlayerId() {
		return currentPlayerId;
	}

	public void setCurrentPlayerId(long currentPlayerId) {
		this.currentPlayerId = currentPlayerId;
	}

	public int getP1OneMoveAgoX() {
		return p1OneMoveAgoX;
	}

	public void setP1OneMoveAgoX(int p1OneMoveAgoX) {
		this.p1OneMoveAgoX = p1OneMoveAgoX;
	}

	public int getP1OneMoveAgoY() {
		return p1OneMoveAgoY;
	}

	public void setP1OneMoveAgoY(int p1OneMoveAgoY) {
		this.p1OneMoveAgoY = p1OneMoveAgoY;
	}

	public int getP1TwoMoveAgoX() {
		return p1TwoMoveAgoX;
	}

	public void setP1TwoMoveAgoX(int p1TwoMoveAgoX) {
		this.p1TwoMoveAgoX = p1TwoMoveAgoX;
	}

	public int getP1TwoMoveAgoY() {
		return p1TwoMoveAgoY;
	}

	public void setP1TwoMoveAgoY(int p1TwoMoveAgoY) {
		this.p1TwoMoveAgoY = p1TwoMoveAgoY;
	}

	public int getP1OneIdAgo() {
		return p1OneIdAgo;
	}

	public void setP1OneIdAgo(int p1OneIdAgo) {
		this.p1OneIdAgo = p1OneIdAgo;
	}

	public int getP1TwoIdAgo() {
		return p1TwoIdAgo;
	}

	public void setP1TwoIdAgo(int p1TwoIdAgo) {
		this.p1TwoIdAgo = p1TwoIdAgo;
	}

	public int getP2OneMoveAgoX() {
		return p2OneMoveAgoX;
	}

	public void setP2OneMoveAgoX(int p2OneMoveAgoX) {
		this.p2OneMoveAgoX = p2OneMoveAgoX;
	}

	public int getP2OneMoveAgoY() {
		return p2OneMoveAgoY;
	}

	public void setP2OneMoveAgoY(int p2OneMoveAgoY) {
		this.p2OneMoveAgoY = p2OneMoveAgoY;
	}

	public int getP2TwoMoveAgoX() {
		return p2TwoMoveAgoX;
	}

	public void setP2TwoMoveAgoX(int p2TwoMoveAgoX) {
		this.p2TwoMoveAgoX = p2TwoMoveAgoX;
	}

	public int getP2TwoMoveAgoY() {
		return p2TwoMoveAgoY;
	}

	public void setP2TwoMoveAgoY(int p2TwoMoveAgoY) {
		this.p2TwoMoveAgoY = p2TwoMoveAgoY;
	}

	public int getP2OneIdAgo() {
		return p2OneIdAgo;
	}

	public void setP2OneIdAgo(int p2OneIdAgo) {
		this.p2OneIdAgo = p2OneIdAgo;
	}

	public int getP2TwoIdAgo() {
		return p2TwoIdAgo;
	}

	public void setP2TwoIdAgo(int p2TwoIdAgo) {
		this.p2TwoIdAgo = p2TwoIdAgo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((board == null) ? 0 : board.hashCode());
		result = prime * result + (int) (currentPlayerId ^ (currentPlayerId >>> 32));
		result = prime * result + (int) (gameId ^ (gameId >>> 32));
		result = prime * result + ((gameStatus == null) ? 0 : gameStatus.hashCode());
		result = prime * result + p1OneIdAgo;
		result = prime * result + p1OneMoveAgoX;
		result = prime * result + p1OneMoveAgoY;
		result = prime * result + p1TwoIdAgo;
		result = prime * result + p1TwoMoveAgoX;
		result = prime * result + p1TwoMoveAgoY;
		result = prime * result + p2OneIdAgo;
		result = prime * result + p2OneMoveAgoX;
		result = prime * result + p2OneMoveAgoY;
		result = prime * result + p2TwoIdAgo;
		result = prime * result + p2TwoMoveAgoX;
		result = prime * result + p2TwoMoveAgoY;
		result = prime * result + (int) (playerOneId ^ (playerOneId >>> 32));
		result = prime * result + (int) (playerTwoId ^ (playerTwoId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameModel other = (GameModel) obj;
		if (board == null) {
			if (other.board != null)
				return false;
		} else if (!board.equals(other.board))
			return false;
		if (currentPlayerId != other.currentPlayerId)
			return false;
		if (gameId != other.gameId)
			return false;
		if (gameStatus == null) {
			if (other.gameStatus != null)
				return false;
		} else if (!gameStatus.equals(other.gameStatus))
			return false;
		if (p1OneIdAgo != other.p1OneIdAgo)
			return false;
		if (p1OneMoveAgoX != other.p1OneMoveAgoX)
			return false;
		if (p1OneMoveAgoY != other.p1OneMoveAgoY)
			return false;
		if (p1TwoIdAgo != other.p1TwoIdAgo)
			return false;
		if (p1TwoMoveAgoX != other.p1TwoMoveAgoX)
			return false;
		if (p1TwoMoveAgoY != other.p1TwoMoveAgoY)
			return false;
		if (p2OneIdAgo != other.p2OneIdAgo)
			return false;
		if (p2OneMoveAgoX != other.p2OneMoveAgoX)
			return false;
		if (p2OneMoveAgoY != other.p2OneMoveAgoY)
			return false;
		if (p2TwoIdAgo != other.p2TwoIdAgo)
			return false;
		if (p2TwoMoveAgoX != other.p2TwoMoveAgoX)
			return false;
		if (p2TwoMoveAgoY != other.p2TwoMoveAgoY)
			return false;
		if (playerOneId != other.playerOneId)
			return false;
		if (playerTwoId != other.playerTwoId)
			return false;
		return true;
	}
}

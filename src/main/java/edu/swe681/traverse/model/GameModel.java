package edu.swe681.traverse.model;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.swe681.traverse.game.GameBoard;
import edu.swe681.traverse.game.enums.GameStatus;

/**
 * Represents a game model in the database. Easily mapped from an
 * instance of a GameBoard.
 */
public class GameModel {
	
	//TODO: Fixed all the types, but do all the nullables need an annotation now?
	// game board
	private String board; // serialized, nullable when there is only one player
	private long gameId;
	private Long playerOneId;
	private Long playerTwoId; // nullable
	
	// game state
	private GameStatus gameStatus;
	private Long currentPlayerId; // nullable

	// p1MoveHistory
	private Integer p1OneMoveAgoX; // nullable
	private Integer p1OneMoveAgoY; // nullable
	private Integer p1TwoMoveAgoX; // nullable
	private Integer p1TwoMoveAgoY; // nullable
	private Integer p1OneIdAgo; // nullable
	private Integer p1TwoIdAgo; // nullable
	
	// p2MoveHistory
	private Integer p2OneMoveAgoX; // nullable
	private Integer p2OneMoveAgoY; // nullable
	private Integer p2TwoMoveAgoX; // nullable
	private Integer p2TwoMoveAgoY; // nullable
	private Integer p2OneIdAgo; // nullable
	private Integer p2TwoIdAgo; // nullable
	
	public GameModel(){}
	
	public GameModel(GameBoard board) throws JsonProcessingException{
		Objects.requireNonNull(board, "requires GameBoard");
		
		// game board
		this.board = serialize2dIntArray(board.getBoard());
		this.gameId = board.getGameID();
		this.playerOneId = board.getPlayerOneID();
		this.playerTwoId = board.getPlayerTwoID(); 
		
		// game state
		this.gameStatus = board.getGameState().getStatus();
		this.currentPlayerId = board.getGameState().getCurrentPlayerID();
		
		// p1MoveHistory
		if (board.getMoveHistory(board.getPlayerOneID()).getOneMoveAgo() != null)
		{
			this.p1OneMoveAgoX = board.getMoveHistory(board.getPlayerOneID()).getOneMoveAgo().x;
			this.p1OneMoveAgoY = board.getMoveHistory(board.getPlayerOneID()).getOneMoveAgo().y;
		}
		else
		{
			this.p1OneMoveAgoX = null;
			this.p1OneMoveAgoY = null;
		}
		if (board.getMoveHistory(board.getPlayerOneID()).getTwoMoveAgo() != null)
		{
			this.p1TwoMoveAgoX = board.getMoveHistory(board.getPlayerOneID()).getTwoMoveAgo().x;
			this.p1TwoMoveAgoY = board.getMoveHistory(board.getPlayerOneID()).getTwoMoveAgo().y;
		}
		else
		{
			this.p1TwoMoveAgoX = null;
			this.p1TwoMoveAgoY = null;
		}
		this.p1OneIdAgo = board.getMoveHistory(board.getPlayerOneID()).getOneIDAgo();
		this.p1TwoIdAgo = board.getMoveHistory(board.getPlayerOneID()).getTwoIDAgo();
		
		// p2MoveHistory
		if (board.getMoveHistory(board.getPlayerOneID()).getOneMoveAgo() != null)
		{
			this.p2OneMoveAgoX = board.getMoveHistory(board.getPlayerOneID()).getOneMoveAgo().x;
			this.p2OneMoveAgoY = board.getMoveHistory(board.getPlayerOneID()).getOneMoveAgo().y;
		}
		else
		{
			this.p2OneMoveAgoX = null;
			this.p2OneMoveAgoY = null;
		}
		if (board.getMoveHistory(board.getPlayerOneID()).getTwoMoveAgo() != null)
		{
			this.p2TwoMoveAgoX = board.getMoveHistory(board.getPlayerOneID()).getTwoMoveAgo().x;
			this.p2TwoMoveAgoY = board.getMoveHistory(board.getPlayerOneID()).getTwoMoveAgo().y;
		}
		else
		{
			this.p2TwoMoveAgoX = null;
			this.p2TwoMoveAgoY = null;
		}
		this.p2OneIdAgo = board.getMoveHistory(board.getPlayerTwoID()).getOneIDAgo();
		this.p2TwoIdAgo = board.getMoveHistory(board.getPlayerTwoID()).getTwoIDAgo();
	}

	public int[][] boardAsArray() throws JsonParseException, JsonMappingException, IOException {
		return deserialize2dIntArray(this.board);
	}
	
	private String serialize2dIntArray(int[][] arr) throws JsonProcessingException{
		return new ObjectMapper().writeValueAsString(arr);
	}
	
	private int[][] deserialize2dIntArray(String json) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(json, int[][].class);
	}
	
	public boolean isUserInGame(long userId) {
		return (playerOneId != null && playerOneId == userId) || (playerTwoId != null && playerTwoId == userId);
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

	public Long getPlayerOneId() {
		return playerOneId;
	}

	public void setPlayerOneId(Long playerOneId) {
		this.playerOneId = playerOneId;
	}

	public Long getPlayerTwoId() {
		return playerTwoId;
	}

	public void setPlayerTwoId(Long playerTwoId) {
		this.playerTwoId = playerTwoId;
	}

	public GameStatus getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	public Long getCurrentPlayerId() {
		return currentPlayerId;
	}

	public void setCurrentPlayerId(Long currentPlayerId) {
		this.currentPlayerId = currentPlayerId;
	}

	public Integer getP1OneMoveAgoX() {
		return p1OneMoveAgoX;
	}

	public void setP1OneMoveAgoX(Integer p1OneMoveAgoX) {
		this.p1OneMoveAgoX = p1OneMoveAgoX;
	}

	public Integer getP1OneMoveAgoY() {
		return p1OneMoveAgoY;
	}

	public void setP1OneMoveAgoY(Integer p1OneMoveAgoY) {
		this.p1OneMoveAgoY = p1OneMoveAgoY;
	}

	public Integer getP1TwoMoveAgoX() {
		return p1TwoMoveAgoX;
	}

	public void setP1TwoMoveAgoX(Integer p1TwoMoveAgoX) {
		this.p1TwoMoveAgoX = p1TwoMoveAgoX;
	}

	public Integer getP1TwoMoveAgoY() {
		return p1TwoMoveAgoY;
	}

	public void setP1TwoMoveAgoY(Integer p1TwoMoveAgoY) {
		this.p1TwoMoveAgoY = p1TwoMoveAgoY;
	}

	public Integer getP1OneIdAgo() {
		return p1OneIdAgo;
	}

	public void setP1OneIdAgo(Integer p1OneIdAgo) {
		this.p1OneIdAgo = p1OneIdAgo;
	}

	public Integer getP1TwoIdAgo() {
		return p1TwoIdAgo;
	}

	public void setP1TwoIdAgo(Integer p1TwoIdAgo) {
		this.p1TwoIdAgo = p1TwoIdAgo;
	}

	public Integer getP2OneMoveAgoX() {
		return p2OneMoveAgoX;
	}

	public void setP2OneMoveAgoX(Integer p2OneMoveAgoX) {
		this.p2OneMoveAgoX = p2OneMoveAgoX;
	}

	public Integer getP2OneMoveAgoY() {
		return p2OneMoveAgoY;
	}

	public void setP2OneMoveAgoY(Integer p2OneMoveAgoY) {
		this.p2OneMoveAgoY = p2OneMoveAgoY;
	}

	public Integer getP2TwoMoveAgoX() {
		return p2TwoMoveAgoX;
	}

	public void setP2TwoMoveAgoX(Integer p2TwoMoveAgoX) {
		this.p2TwoMoveAgoX = p2TwoMoveAgoX;
	}

	public Integer getP2TwoMoveAgoY() {
		return p2TwoMoveAgoY;
	}

	public void setP2TwoMoveAgoY(Integer p2TwoMoveAgoY) {
		this.p2TwoMoveAgoY = p2TwoMoveAgoY;
	}

	public Integer getP2OneIdAgo() {
		return p2OneIdAgo;
	}

	public void setP2OneIdAgo(Integer p2OneIdAgo) {
		this.p2OneIdAgo = p2OneIdAgo;
	}

	public Integer getP2TwoIdAgo() {
		return p2TwoIdAgo;
	}

	public void setP2TwoIdAgo(Integer p2TwoIdAgo) {
		this.p2TwoIdAgo = p2TwoIdAgo;
	}

	@Override
	public String toString() {
		return "GameModel [board=" + board + ", gameId=" + gameId + ", playerOneId=" + playerOneId + ", playerTwoId="
				+ playerTwoId + ", gameStatus=" + gameStatus + ", currentPlayerId=" + currentPlayerId
				+ ", p1OneMoveAgoX=" + p1OneMoveAgoX + ", p1OneMoveAgoY=" + p1OneMoveAgoY + ", p1TwoMoveAgoX="
				+ p1TwoMoveAgoX + ", p1TwoMoveAgoY=" + p1TwoMoveAgoY + ", p1OneIdAgo=" + p1OneIdAgo + ", p1TwoIdAgo="
				+ p1TwoIdAgo + ", p2OneMoveAgoX=" + p2OneMoveAgoX + ", p2OneMoveAgoY=" + p2OneMoveAgoY
				+ ", p2TwoMoveAgoX=" + p2TwoMoveAgoX + ", p2TwoMoveAgoY=" + p2TwoMoveAgoY + ", p2OneIdAgo=" + p2OneIdAgo
				+ ", p2TwoIdAgo=" + p2TwoIdAgo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((board == null) ? 0 : board.hashCode());
		result = prime * result + ((currentPlayerId == null) ? 0 : currentPlayerId.hashCode());
		result = prime * result + (int) (gameId ^ (gameId >>> 32));
		result = prime * result + ((gameStatus == null) ? 0 : gameStatus.hashCode());
		result = prime * result + ((p1OneIdAgo == null) ? 0 : p1OneIdAgo.hashCode());
		result = prime * result + ((p1OneMoveAgoX == null) ? 0 : p1OneMoveAgoX.hashCode());
		result = prime * result + ((p1OneMoveAgoY == null) ? 0 : p1OneMoveAgoY.hashCode());
		result = prime * result + ((p1TwoIdAgo == null) ? 0 : p1TwoIdAgo.hashCode());
		result = prime * result + ((p1TwoMoveAgoX == null) ? 0 : p1TwoMoveAgoX.hashCode());
		result = prime * result + ((p1TwoMoveAgoY == null) ? 0 : p1TwoMoveAgoY.hashCode());
		result = prime * result + ((p2OneIdAgo == null) ? 0 : p2OneIdAgo.hashCode());
		result = prime * result + ((p2OneMoveAgoX == null) ? 0 : p2OneMoveAgoX.hashCode());
		result = prime * result + ((p2OneMoveAgoY == null) ? 0 : p2OneMoveAgoY.hashCode());
		result = prime * result + ((p2TwoIdAgo == null) ? 0 : p2TwoIdAgo.hashCode());
		result = prime * result + ((p2TwoMoveAgoX == null) ? 0 : p2TwoMoveAgoX.hashCode());
		result = prime * result + ((p2TwoMoveAgoY == null) ? 0 : p2TwoMoveAgoY.hashCode());
		result = prime * result + ((playerOneId == null) ? 0 : playerOneId.hashCode());
		result = prime * result + ((playerTwoId == null) ? 0 : playerTwoId.hashCode());
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
		if (currentPlayerId == null) {
			if (other.currentPlayerId != null)
				return false;
		} else if (!currentPlayerId.equals(other.currentPlayerId))
			return false;
		if (gameId != other.gameId)
			return false;
		if (gameStatus != other.gameStatus)
			return false;
		if (p1OneIdAgo == null) {
			if (other.p1OneIdAgo != null)
				return false;
		} else if (!p1OneIdAgo.equals(other.p1OneIdAgo))
			return false;
		if (p1OneMoveAgoX == null) {
			if (other.p1OneMoveAgoX != null)
				return false;
		} else if (!p1OneMoveAgoX.equals(other.p1OneMoveAgoX))
			return false;
		if (p1OneMoveAgoY == null) {
			if (other.p1OneMoveAgoY != null)
				return false;
		} else if (!p1OneMoveAgoY.equals(other.p1OneMoveAgoY))
			return false;
		if (p1TwoIdAgo == null) {
			if (other.p1TwoIdAgo != null)
				return false;
		} else if (!p1TwoIdAgo.equals(other.p1TwoIdAgo))
			return false;
		if (p1TwoMoveAgoX == null) {
			if (other.p1TwoMoveAgoX != null)
				return false;
		} else if (!p1TwoMoveAgoX.equals(other.p1TwoMoveAgoX))
			return false;
		if (p1TwoMoveAgoY == null) {
			if (other.p1TwoMoveAgoY != null)
				return false;
		} else if (!p1TwoMoveAgoY.equals(other.p1TwoMoveAgoY))
			return false;
		if (p2OneIdAgo == null) {
			if (other.p2OneIdAgo != null)
				return false;
		} else if (!p2OneIdAgo.equals(other.p2OneIdAgo))
			return false;
		if (p2OneMoveAgoX == null) {
			if (other.p2OneMoveAgoX != null)
				return false;
		} else if (!p2OneMoveAgoX.equals(other.p2OneMoveAgoX))
			return false;
		if (p2OneMoveAgoY == null) {
			if (other.p2OneMoveAgoY != null)
				return false;
		} else if (!p2OneMoveAgoY.equals(other.p2OneMoveAgoY))
			return false;
		if (p2TwoIdAgo == null) {
			if (other.p2TwoIdAgo != null)
				return false;
		} else if (!p2TwoIdAgo.equals(other.p2TwoIdAgo))
			return false;
		if (p2TwoMoveAgoX == null) {
			if (other.p2TwoMoveAgoX != null)
				return false;
		} else if (!p2TwoMoveAgoX.equals(other.p2TwoMoveAgoX))
			return false;
		if (p2TwoMoveAgoY == null) {
			if (other.p2TwoMoveAgoY != null)
				return false;
		} else if (!p2TwoMoveAgoY.equals(other.p2TwoMoveAgoY))
			return false;
		if (playerOneId == null) {
			if (other.playerOneId != null)
				return false;
		} else if (!playerOneId.equals(other.playerOneId))
			return false;
		if (playerTwoId == null) {
			if (other.playerTwoId != null)
				return false;
		} else if (!playerTwoId.equals(other.playerTwoId))
			return false;
		return true;
	}
}

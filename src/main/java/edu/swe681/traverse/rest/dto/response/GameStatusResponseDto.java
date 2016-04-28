package edu.swe681.traverse.rest.dto.response;

import java.util.Arrays;
import java.util.Objects;

import edu.swe681.traverse.game.GameBoard;
import edu.swe681.traverse.game.enums.GameStatus;

/**
 * Response DTO that has the current state of the game:
 * -2d array representing the board and pieceIds on it
 * -current player id
 * -ids of players 1 and 2
 * -status of the game
 */
public class GameStatusResponseDto extends GameResponseDto {
	private final int[][] board;
	private final Long currentPlayerId;
	private final long player1Id;
	private final Long player2Id;
	private final String status;

	public GameStatusResponseDto(long gameId, int[][] board, Long currentPlayerId, long player1Id, Long player2Id, GameStatus status) {
		super(gameId);
		this.board = Objects.requireNonNull(board, "board is required");
		this.currentPlayerId = currentPlayerId;
		this.player1Id = player1Id;
		this.player2Id = player2Id;
		this.status = Objects.requireNonNull(status.toString(), "status is required");
	}

	public int[][] getBoard() {
		int[][] retBoard = new int[GameBoard.SIZE][GameBoard.SIZE];
		for (int i = 0; i < GameBoard.SIZE; i++)
		{
			for (int j = 0; j < GameBoard.SIZE; j++)
			{
				retBoard[i][j] = this.board[i][j];
			}
		}
		
		return retBoard;
	}

	public Long getCurrentPlayerId() {
		return currentPlayerId;
	}

	public long getPlayer1Id() {
		return player1Id;
	}

	public Long getPlayer2Id() {
		return player2Id;
	}

	public String getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "GameStatusResponseDto [board=" + Arrays.toString(board) + ", currentPlayerId=" + currentPlayerId
				+ ", player1Id=" + player1Id + ", player2Id=" + player2Id + ", status=" + status + ", gameId="
				+ getGameId() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.deepHashCode(board);
		result = prime * result + ((currentPlayerId == null) ? 0 : currentPlayerId.hashCode());
		result = prime * result + (int) (player1Id ^ (player1Id >>> 32));
		result = prime * result + ((player2Id == null) ? 0 : player2Id.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameStatusResponseDto other = (GameStatusResponseDto) obj;
		if (!Arrays.deepEquals(board, other.board))
			return false;
		if (currentPlayerId == null) {
			if (other.currentPlayerId != null)
				return false;
		} else if (!currentPlayerId.equals(other.currentPlayerId))
			return false;
		if (player1Id != other.player1Id)
			return false;
		if (player2Id == null) {
			if (other.player2Id != null)
				return false;
		} else if (!player2Id.equals(other.player2Id))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
}

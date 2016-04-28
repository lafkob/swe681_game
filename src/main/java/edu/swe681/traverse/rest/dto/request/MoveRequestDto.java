package edu.swe681.traverse.rest.dto.request;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import edu.swe681.traverse.game.Game;
import edu.swe681.traverse.rest.dto.Coordinate;

/**
 * Data for a request to make a move for a game.
 */
public class MoveRequestDto extends GameRequestDto {

	/**
	 * Represents the ID of the piece to move. Note: the game must validate the
	 * pieceId belongs to the current owner.
	 */
	@NotNull
	@Min(0)
	@Max(Game.NUM_PIECES - 1)
	public Integer pieceId;

	/**
	 * Represents the ordered list of moves requested by the client.
	 */
	@NotNull
	@Size(min=1, max=10)
	public List<Coordinate> moves;

	public MoveRequestDto() {
		super();
	}

	public MoveRequestDto(Long gameId, Integer pieceId, List<Coordinate> moves) {
		super(gameId);
		this.pieceId = pieceId;
		this.moves = moves;
	}

	public Integer getPieceId() {
		return pieceId;
	}

	public void setPieceId(Integer pieceId) {
		this.pieceId = pieceId;
	}

	public List<Coordinate> getMoves() {
		return moves;
	}

	public void setMoves(List<Coordinate> moves) {
		this.moves = moves;
	}

	@Override
	public String toString() {
		return "MoveRequestDto [pieceId=" + pieceId + ", moves=" + moves + ", gameId=" + getGameId() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((moves == null) ? 0 : moves.hashCode());
		result = prime * result + ((pieceId == null) ? 0 : pieceId.hashCode());
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
		MoveRequestDto other = (MoveRequestDto) obj;
		if (moves == null) {
			if (other.moves != null)
				return false;
		} else if (!moves.equals(other.moves))
			return false;
		if (pieceId == null) {
			if (other.pieceId != null)
				return false;
		} else if (!pieceId.equals(other.pieceId))
			return false;
		return true;
	}
}

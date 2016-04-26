package edu.swe681.traverse.rest.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Superclass for all game-centric requests. Gives functionality for gameId
 */
public class GameRequestDto {
	
	@NotNull
	@Min(1) // FKs start at 1!
	private Long gameId;
	
	public GameRequestDto(){}

	public GameRequestDto(Long gameId) {
		super();
		this.gameId = gameId;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
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
		GameRequestDto other = (GameRequestDto) obj;
		if (gameId == null) {
			if (other.gameId != null)
				return false;
		} else if (!gameId.equals(other.gameId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GameRequestDto [gameId=" + gameId + "]";
	}
}

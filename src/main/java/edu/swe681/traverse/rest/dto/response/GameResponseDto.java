package edu.swe681.traverse.rest.dto.response;

/**
 * Base response dto that includes a gameId.
 */
public class GameResponseDto {

	private final long gameId;
	
	public GameResponseDto(long gameId) {
		super();
		this.gameId = gameId;
	}

	public long getGameId() {
		return gameId;
	}

	@Override
	public String toString() {
		return "GameResponseDto [gameId=" + gameId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (gameId ^ (gameId >>> 32));
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
		GameResponseDto other = (GameResponseDto) obj;
		if (gameId != other.gameId)
			return false;
		return true;
	}
}

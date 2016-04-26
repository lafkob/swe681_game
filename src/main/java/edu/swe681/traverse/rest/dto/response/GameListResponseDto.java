package edu.swe681.traverse.rest.dto.response;

import java.util.ArrayList;
import java.util.List;

public class GameListResponseDto {
	public List<Long> gameIds = new ArrayList<>();
	
	public GameListResponseDto(List<Long> gameIds) {
		if(gameIds != null){
			this.gameIds = gameIds;
		}
	}

	public List<Long> getGameIds() {
		return gameIds;
	}

	@Override
	public String toString() {
		return "GameListResponseDto [gameIds=" + gameIds + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gameIds == null) ? 0 : gameIds.hashCode());
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
		GameListResponseDto other = (GameListResponseDto) obj;
		if (gameIds == null) {
			if (other.gameIds != null)
				return false;
		} else if (!gameIds.equals(other.gameIds))
			return false;
		return true;
	}
}

package edu.swe681.traverse.rest.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Wraps a list of UserInfo so it can easily be JSON serde'ed.
 */
public class UserInfoListResponseDto {
	private List<UserInfoResponseDto> userInfo = new ArrayList<>();
	
	public UserInfoListResponseDto(List<UserInfoResponseDto> userInfo) {
		if(userInfo != null) this.userInfo = userInfo;
	}
	public List<UserInfoResponseDto> getUserInfo() {
		return userInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userInfo == null) ? 0 : userInfo.hashCode());
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
		UserInfoListResponseDto other = (UserInfoListResponseDto) obj;
		if (userInfo == null) {
			if (other.userInfo != null)
				return false;
		} else if (!userInfo.equals(other.userInfo))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "UserInfoListResponseDto [userInfo=" + userInfo + "]";
	}

	
	
	/**
	 * Wrapper for public information about a user: username, wins, losses.
	 */
	public static class UserInfoResponseDto{
		private String username;
		private Integer wins = 0;
		private Integer losses = 0;
		
		public UserInfoResponseDto(String username, Integer wins, Integer losses) {
			this.username = Objects.requireNonNull(username, "username required");
			if(wins != null) this.wins = wins;
			if(losses != null) this.losses = losses;
		}

		public String getUsername() {
			return username;
		}

		public Integer getWins() {
			return wins;
		}

		public Integer getLosses() {
			return losses;
		}

		@Override
		public String toString() {
			return "UserInfo [username=" + username + ", wins=" + wins + ", losses=" + losses + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((losses == null) ? 0 : losses.hashCode());
			result = prime * result + ((username == null) ? 0 : username.hashCode());
			result = prime * result + ((wins == null) ? 0 : wins.hashCode());
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
			UserInfoResponseDto other = (UserInfoResponseDto) obj;
			if (losses == null) {
				if (other.losses != null)
					return false;
			} else if (!losses.equals(other.losses))
				return false;
			if (username == null) {
				if (other.username != null)
					return false;
			} else if (!username.equals(other.username))
				return false;
			if (wins == null) {
				if (other.wins != null)
					return false;
			} else if (!wins.equals(other.wins))
				return false;
			return true;
		}
	}
}

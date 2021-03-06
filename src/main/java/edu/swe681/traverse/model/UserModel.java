package edu.swe681.traverse.model;

/**
 * POJO to represent the User model.
 */
public class UserModel {
	private long id;
	private String username;
	private String passwordHash;
	private int authAttempts;
	
	public UserModel(){}

	public UserModel(long id, String username, String passwordHash, int authAttempts) {
		super();
		this.id = id;
		this.username = username;
		this.passwordHash = passwordHash;
		this.authAttempts = authAttempts;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public int getAuthAttempts() {
		return authAttempts;
	}

	public void setAuthAttempts(int authAttempts) {
		this.authAttempts = authAttempts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + authAttempts;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((passwordHash == null) ? 0 : passwordHash.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		UserModel other = (UserModel) obj;
		if (authAttempts != other.authAttempts)
			return false;
		if (id != other.id)
			return false;
		if (passwordHash == null) {
			if (other.passwordHash != null)
				return false;
		} else if (!passwordHash.equals(other.passwordHash))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserModel [id=" + id + ", username=" + username + ", passwordHash=" + passwordHash + ", authAttempts="
				+ authAttempts + "]";
	}
}

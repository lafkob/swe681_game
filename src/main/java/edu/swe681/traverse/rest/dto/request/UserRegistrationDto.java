package edu.swe681.traverse.rest.dto.request;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * DTO for information submitted for user registration.
 */
public class UserRegistrationDto {
	
	@NotBlank
	@Size(min=8, max=32, message="Username must be between 8 and 32 characters")
	private String username;

	@NotBlank
	@Size(min=10, max=32, message="Password must be between 10 and 32 characters")
	private String password;

	@NotBlank
	@Size(min=10, max=32, message="Password confirmation must be between 10 and 32 characters and match password")
	private String passwordConfirm;

	public UserRegistrationDto(String username, String password, String passwordConfirm) {
		super();
		this.username = username;
		this.password = password;
		this.passwordConfirm = passwordConfirm;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((passwordConfirm == null) ? 0 : passwordConfirm.hashCode());
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
		UserRegistrationDto other = (UserRegistrationDto) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (passwordConfirm == null) {
			if (other.passwordConfirm != null)
				return false;
		} else if (!passwordConfirm.equals(other.passwordConfirm))
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
		return "UserRegistrationDto [username=" + username + ", password=" + password + ", passwordConfirm="
				+ passwordConfirm + "]";
	}
}

package edu.swe681.traverse.rest.dto.request;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * DTO for information submitted for user registration.
 */
public class UserRegistrationRequestDto {
	
	@NotBlank
	@Size(min=8, max=32)
	@Pattern(regexp="^[A-Za-z0-9]*$", message="usernames can only contain letters and numbers")
	private String username;

	@NotBlank
	@Size(min=10, max=32)
	@Pattern(regexp="^[A-Za-z0-9_!#$%+=@()]*$", message="passwords can only contain letters, numbers, and characters _ ! # $ % + = @ ( )")
	private String password;

	@NotBlank
	@Size(min=10, max=32)
	@Pattern(regexp="^[A-Za-z0-9_!#$%+=@()]*$", message="passwords can only contain letters, numbers, and characters _ ! # $ % + = @ ( )")
	private String passwordConfirm;
	
	public UserRegistrationRequestDto(){}

	public UserRegistrationRequestDto(String username, String password, String passwordConfirm) {
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
		UserRegistrationRequestDto other = (UserRegistrationRequestDto) obj;
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

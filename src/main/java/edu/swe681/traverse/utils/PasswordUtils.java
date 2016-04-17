package edu.swe681.traverse.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilities for handling passwords
 */
public class PasswordUtils {
	
	/**
	 * Creates a hash from a password.
	 * 
	 * @param password
	 * @return
	 */
	public static String hashPassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
		String hashedPassword = passwordEncoder.encode(password);
		
		return hashedPassword;
	}
}

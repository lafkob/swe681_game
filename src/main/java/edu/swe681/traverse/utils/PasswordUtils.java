package edu.swe681.traverse.utils;

import java.util.Date;

/**
 * Utilities for handling passwords
 */
public class PasswordUtils {

	/**
	 * Generates a salt to be used in password hashing.
	 * 
	 * @return A salt
	 */
	public static String generateSalt() {
		// TODO:
		return new Date().toString();
	}
	
	/**
	 * Creates a hash from a password and salt.
	 * 
	 * @param password
	 * @param salt
	 * @return
	 */
	public static String hashPassword(String password, String salt) {
		// TODO
		return password + salt;
	}
}

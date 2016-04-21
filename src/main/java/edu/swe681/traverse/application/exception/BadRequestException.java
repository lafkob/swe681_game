package edu.swe681.traverse.application.exception;

/**
 * Exception type to indicate a BadRequest (400).
 */
public class BadRequestException extends Exception {

	private static final long serialVersionUID = -7564754837360672903L;

	public BadRequestException(String message) {
		super(message);
	}
}

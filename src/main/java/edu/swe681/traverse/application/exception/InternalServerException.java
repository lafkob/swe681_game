package edu.swe681.traverse.application.exception;

/**
 * Exception type to indicate an InternalServerError (500).
 */
public class InternalServerException extends Exception {

	private static final long serialVersionUID = 2567320943537675652L;

	public InternalServerException(String message) {
		super(message);
	}
}

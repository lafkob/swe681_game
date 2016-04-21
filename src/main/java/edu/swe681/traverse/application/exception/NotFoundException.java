package edu.swe681.traverse.application.exception;

/**
 * Exception type to indicate NotFound (404).
 */
public class NotFoundException extends Exception {

	private static final long serialVersionUID = -8414304750479518791L;

	public NotFoundException(String message) {
		super(message);
	}
}

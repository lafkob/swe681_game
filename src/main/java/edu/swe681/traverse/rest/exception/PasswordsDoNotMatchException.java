package edu.swe681.traverse.rest.exception;

/**
 * Exception to throw if the passwords given during registration do not
 * match.
 */
public class PasswordsDoNotMatchException extends TraverseRestException
{
	private static final long serialVersionUID = -4003494368715730280L;

	public PasswordsDoNotMatchException()
	{
		super("Error: The passwords do not match.");
	}
	
	public PasswordsDoNotMatchException(String message)
	{
		super(message);
	}
}

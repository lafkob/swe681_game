package edu.swe681.traverse.rest.exception;

/**
 * Exception to throw if a registering user already exists.
 *
 */
public class UsernameAlreadyExistsException extends TraverseRestException
{
	private static final long serialVersionUID = -7038570941755640975L;

	public UsernameAlreadyExistsException()
	{
		super("Error: This username already exists.");
	}
	
	public UsernameAlreadyExistsException(String message)
	{
		super(message);
	}
}

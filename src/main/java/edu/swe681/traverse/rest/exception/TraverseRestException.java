package edu.swe681.traverse.rest.exception;

/**
 * An unchecked exception for a Traverse Rest event.
 * TODO: Should these be checked or unchecked?
 */
public abstract class TraverseRestException extends Exception
{
	private static final long serialVersionUID = -1310796824420857616L;

	public TraverseRestException()
	{
		super();
	}
	
	public TraverseRestException(String message)
	{
		super(message);
	}
}

package edu.swe681.traverse.game.exception;

import java.awt.Point;

public class IllegalAreaException extends TraverseException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -881710679626662593L;

	public IllegalAreaException()
	{
		super();
	}
	
	public IllegalAreaException(String message)
	{
		super(message);
	}
	
	public IllegalAreaException(String message, Point start, Point dest)
	{	
		super(message, start, dest);
	}
}

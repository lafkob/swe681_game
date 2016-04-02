package edu.swe681.traverse.game.exception;

import java.awt.Point;

public class InvalidInputException extends TraverseException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1070955234999690284L;

	public InvalidInputException()
	{
		super();
	}
	
	public InvalidInputException(String message)
	{
		super(message);
	}
	
	public InvalidInputException(String message, Point start, Point dest)
	{	
		super(message, start, dest);
	}
}

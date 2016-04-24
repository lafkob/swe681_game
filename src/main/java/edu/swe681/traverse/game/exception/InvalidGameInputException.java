package edu.swe681.traverse.game.exception;

import java.awt.Point;

public class InvalidGameInputException extends TraverseException
{
	private static final long serialVersionUID = -1070955234999690284L;

	public InvalidGameInputException()
	{
		super();
	}
	
	public InvalidGameInputException(String message)
	{
		super(message);
	}
	
	public InvalidGameInputException(String message, Point start, Point dest)
	{	
		super(message, start, dest);
	}
}

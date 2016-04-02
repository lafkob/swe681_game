package edu.swe681.traverse.game.exception;

import java.awt.Point;

public class IllegalStartingMoveException extends TraverseException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1149920087149722747L;

	public IllegalStartingMoveException()
	{
		super();
	}
	
	public IllegalStartingMoveException(String message)
	{
		super(message);
	}
	
	public IllegalStartingMoveException(String message, Point start, Point dest)
	{	
		super(message, start, dest);
	}
}

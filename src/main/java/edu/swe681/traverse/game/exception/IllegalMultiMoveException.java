package edu.swe681.traverse.game.exception;

import java.awt.Point;

public class IllegalMultiMoveException extends TraverseException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6836203982075001288L;

	public IllegalMultiMoveException()
	{
		super();
	}
	
	public IllegalMultiMoveException(String message)
	{
		super(message);
	}
	
	public IllegalMultiMoveException(String message, Point start, Point dest)
	{	
		super(message, start, dest);
	}
}

package edu.swe681.traverse.game.exception;

import java.awt.Point;

/**
 * An exception for the Traverse game engine
 */
public abstract class TraverseException extends Exception
{
	private static final long serialVersionUID = -1277512075278058636L;

	public TraverseException()
	{
		super();
	}
	
	public TraverseException(String message)
	{
		super(message);
	}
	
	public TraverseException(String message, Point start, Point dest)
	{	
		super(String.format("Move from [%d,%d] to [%d,%d] is illegal. ", start.x, start.y,
				dest.x, dest.y) + message);
	}
}

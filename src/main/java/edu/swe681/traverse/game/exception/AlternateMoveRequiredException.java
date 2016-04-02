package edu.swe681.traverse.game.exception;

import java.awt.Point;

public class AlternateMoveRequiredException extends TraverseException
{
	private static final long serialVersionUID = -3878110456728203085L;

	public AlternateMoveRequiredException()
	{
		super();
	}
	
	public AlternateMoveRequiredException(String message)
	{
		super(message);
	}
	
	public AlternateMoveRequiredException(String message, Point start, Point dest)
	{	
		super(message, start, dest);
	}
}

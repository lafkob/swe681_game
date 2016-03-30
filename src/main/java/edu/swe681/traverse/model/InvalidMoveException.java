package edu.swe681.traverse.model;

import java.awt.Point;

/**
 * An exception that occurs when a move is invalid (e.g.
 * the destination is not on the board, the piece ID and
 * location do not match, etc)
 */
public final class InvalidMoveException extends Exception
{
	/* Auto-generated ID */
	private static final long serialVersionUID = -9027184303120224130L;

	public InvalidMoveException()
	{
		super();
	}
	
	public InvalidMoveException(String message)
	{
		super(message);
	}
	
	public InvalidMoveException(String message, Point start, Point dest)
	{	
		super(String.format("Move from [%d,%d] to [%d,%d] is invalid. ", start.x, start.y,
				dest.x, dest.y) + message);
	}
}

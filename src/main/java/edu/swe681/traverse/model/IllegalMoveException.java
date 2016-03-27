package edu.swe681.traverse.model;

import java.awt.Point;

/**
 * An exception that occurs when a move is illegal (e.g. not
 * the player's piece, move not legal for particular piece
 * type, etc)
 */
public final class IllegalMoveException extends Exception
{
	/* Auto-generated ID */
	private static final long serialVersionUID = 9220897921897691892L;

	public IllegalMoveException()
	{
		super();
	}
	
	public IllegalMoveException(String message)
	{
		super(message);
	}
	
	public IllegalMoveException(String message, Point start, Point dest)
	{	
		super(String.format("Move from [%d,%d] to [%d,%d] is illegal. ", start.x, start.y,
				dest.x, dest.y) + message);
	}
}

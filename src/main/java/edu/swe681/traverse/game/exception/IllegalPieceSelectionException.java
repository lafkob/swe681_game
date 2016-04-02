package edu.swe681.traverse.game.exception;

import java.awt.Point;

public class IllegalPieceSelectionException extends TraverseException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 744433494869578523L;

	public IllegalPieceSelectionException()
	{
		super();
	}
	
	public IllegalPieceSelectionException(String message)
	{
		super(message);
	}
	
	public IllegalPieceSelectionException(String message, Point start, Point dest)
	{	
		super(message, start, dest);
	}
}

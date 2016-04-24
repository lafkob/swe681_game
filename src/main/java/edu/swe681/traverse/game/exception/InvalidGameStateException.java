package edu.swe681.traverse.game.exception;

/**
 * Exception to handle calling methods when the game is not in the
 * correct state to do so.
 */
public class InvalidGameStateException extends TraverseException
{
	private static final long serialVersionUID = -5102116879029342205L;

	public InvalidGameStateException()
	{
		super();
	}
	
	public InvalidGameStateException(String message)
	{
		super(message);
	}
}

package edu.swe681.traverse.game;

import edu.swe681.traverse.game.enums.GameStatus;

/**
 * The Traverse Game state, indicating the current status
 * of the game and the ID of the player who's turn it is
 */
public final class GameState
{
	private final GameStatus status;
	private final int currentPlayerID;
	
	/**
	 * Default constructor 
	 * 
	 * @param status Game status
	 * @param playerID Current player's ID
	 */
	public GameState(GameStatus status, int playerID)
	{
		this.status = status;
		this.currentPlayerID = playerID;
	}
	
	/**
	 * Return the current status
	 * 
	 * @return The game's status
	 */
	public GameStatus getStatus()
	{
		return status;
	}
	
	/**
	 * Return the current player
	 * 
	 * @return The current player
	 */
	public int getCurrentPlayerID()
	{
		return currentPlayerID;
	}
	
	/**
	 * Returns a new GameState updated with the given status
	 *  
	 * @param status Next status
	 * @return GameState updated with the given status
	 */
	public GameState updateStatus(GameStatus status)
	{
		return new GameState(status, this.currentPlayerID);
	}
	
	/**
	 * Returns a new GameState updated with the given player
	 * 
	 * @param player Next player
	 * @return GameState updated with the given status
	 */
	public GameState updatePlayer(int currentPlayerID)
	{
		return new GameState(this.status, currentPlayerID);
	}
}

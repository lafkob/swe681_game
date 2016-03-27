package edu.swe681.traverse.model;

/**
 * The Traverse Game state, indicating the current status
 * of the game and which player's turn it is.
 */
public final class GameState
{
	private final GameStatus status;
	private final Player player;
	
	/**
	 * Default constructor 
	 * 
	 * @param status Game status
	 * @param player Current player
	 */
	public GameState(GameStatus status, Player player)
	{
		this.status = status;
		this.player = player;
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
	public Player getPlayer()
	{
		return player;
	}
	
	/**
	 * Returns a new GameState updated with the given status
	 *  
	 * @param status Next status
	 * @return GameState updated with the given status
	 */
	public GameState updateStatus(GameStatus status)
	{
		return new GameState(status, this.player);
	}
	
	/**
	 * Returns a new GameState updated with the given player
	 * 
	 * @param player Next player
	 * @return GameState updated with the given status
	 */
	public GameState updatePlayer(Player player)
	{
		return new GameState(this.status, player);
	}
}

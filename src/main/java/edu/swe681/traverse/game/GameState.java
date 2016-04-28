package edu.swe681.traverse.game;

import edu.swe681.traverse.game.enums.GameStatus;

/**
 * The Traverse Game state, indicating the current status
 * of the game and the ID of the player who's turn it is
 */
public final class GameState
{
	private final GameStatus status;
	/* Should be null when the status is WAITING_FOR_PLAYER_TWO.
	 * If the status is in a WIN or FORFEIT state, this represents
	 * the winner of the completed game. */
	private final Long currentPlayerID;
	
	/**
	 * Default constructor 
	 * 
	 * @param status Game status
	 * @param playerID Current player's ID
	 */
	public GameState(GameStatus status, Long playerID)
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
	public Long getCurrentPlayerID()
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
	public GameState updatePlayer(Long currentPlayerID)
	{
		return new GameState(this.status, currentPlayerID);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentPlayerID == null) ? 0 : currentPlayerID.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameState other = (GameState) obj;
		if (currentPlayerID == null) {
			if (other.currentPlayerID != null)
				return false;
		} else if (!currentPlayerID.equals(other.currentPlayerID))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GameState [status=" + status + ", currentPlayerID=" + currentPlayerID + "]";
	}
}

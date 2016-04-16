package edu.swe681.traverse.game.enums;

/**
 * Represents a particular state of the game.
 */
public enum GameStatus {
	// TODO: do we need a finished state rather than WIN? (Why would FINISHED be clearer than WIN?)
	/**
	 * The state before the game has begun, when the first player is waiting for
	 * a second to join.
	 */
	WAITING_FOR_PLAYER_TWO,
	/**
	 * The primary state
	 */
	PLAY,
	/**
	 * The state in which one player has one and no more move can be made
	 */
	WIN;
}

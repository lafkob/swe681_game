package edu.swe681.traverse.game;

import edu.swe681.traverse.game.enums.GamePieceType;
import edu.swe681.traverse.game.enums.Player;

/**
 * Represents a traverse game piece
 */
public final class GamePiece
{
	private final GamePieceType pieceType;
	private final Player player;

	public GamePiece(GamePieceType pieceType, Player player) {
		this.pieceType = pieceType;
		this.player = player;
	}
	
	public GamePieceType getPieceType() {
		return this.pieceType;
	}
	
	public Player getPlayer() {
		return this.player;
	}
}

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

	@Override
	public String toString() {
		return "GamePiece [pieceType=" + pieceType + ", player=" + player + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pieceType == null) ? 0 : pieceType.hashCode());
		result = prime * result + ((player == null) ? 0 : player.hashCode());
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
		GamePiece other = (GamePiece) obj;
		if (pieceType != other.pieceType)
			return false;
		if (player != other.player)
			return false;
		return true;
	}
}

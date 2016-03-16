package edu.swe681.traverse.model;

/**
 * Represents a traverse game piece
 */
public class GamePiece {
	private final GamePieceType pieceType;

	public GamePiece(GamePieceType pieceType) {
		this.pieceType = pieceType;
	}
	
	public GamePieceType getPieceType() {
		return this.pieceType;
	}
}

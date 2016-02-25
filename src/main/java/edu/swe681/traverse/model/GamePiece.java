package edu.swe681.traverse.model;

/**
 * Represents a traverse game piece
 */
public class GamePiece {
	private final PieceType type;

	public GamePiece(PieceType type) {
		this.type = type;
	}

	public static enum PieceType {
		SQUARE, CIRCLE, DIAMOND, TRIANGLE;
	}
}

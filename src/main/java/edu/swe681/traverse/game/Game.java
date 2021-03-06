package edu.swe681.traverse.game;

import edu.swe681.traverse.game.enums.GamePieceType;
import edu.swe681.traverse.game.enums.Player;

/**
 * (Might want to change this name?)
 * Global data for Traverse game
 *
 */
public final class Game
{	
	public static final int NUM_PIECES = 16;
	
	static final GamePiece [] PIECES = {
		new GamePiece(GamePieceType.CIRCLE, Player.ONE),
		new GamePiece(GamePieceType.CIRCLE, Player.ONE),
		new GamePiece(GamePieceType.DIAMOND, Player.ONE),
		new GamePiece(GamePieceType.DIAMOND, Player.ONE),
		new GamePiece(GamePieceType.SQUARE, Player.ONE),
		new GamePiece(GamePieceType.SQUARE, Player.ONE),
		new GamePiece(GamePieceType.TRIANGLE, Player.ONE),
		new GamePiece(GamePieceType.TRIANGLE, Player.ONE),
		new GamePiece(GamePieceType.CIRCLE, Player.TWO),
		new GamePiece(GamePieceType.CIRCLE, Player.TWO),
		new GamePiece(GamePieceType.DIAMOND, Player.TWO),
		new GamePiece(GamePieceType.DIAMOND, Player.TWO),
		new GamePiece(GamePieceType.SQUARE, Player.TWO),
		new GamePiece(GamePieceType.SQUARE, Player.TWO),
		new GamePiece(GamePieceType.TRIANGLE, Player.TWO),
		new GamePiece(GamePieceType.TRIANGLE, Player.TWO)
	};
}

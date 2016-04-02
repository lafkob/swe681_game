package edu.swe681.traverse.game;

import java.awt.Point;

public final class MoveHistory
{
	private Point oneMoveAgo;
	private Point twoMoveAgo;
	private int oneIDAgo;
	private int twoIDAgo;

	public MoveHistory(Point oneMoveAgo, int oneIDAgo, Point twoMoveAgo, int twoIDAgo)
	{
		this.oneMoveAgo = oneMoveAgo;
		this.oneIDAgo = oneIDAgo;
		this.twoMoveAgo = twoMoveAgo;
		this.twoIDAgo = twoIDAgo;
	}

	public Point getOneMoveAgo() {
		return oneMoveAgo;
	}

	public Point getTwoMoveAgo() {
		return twoMoveAgo;
	}

	public int getOneIDAgo() {
		return oneIDAgo;
	}

	public int getTwoIDAgo() {
		return twoIDAgo;
	}
	
	public void setOneMoveAgo(Point oneMoveAgo) {
		this.oneMoveAgo = oneMoveAgo;
	}

	public void setTwoMoveAgo(Point twoMoveAgo) {
		this.twoMoveAgo = twoMoveAgo;
	}

	public void setOneIDAgo(int oneIDAgo) {
		this.oneIDAgo = oneIDAgo;
	}

	public void setTwoIDAgo(int twoIDAgo) {
		this.twoIDAgo = twoIDAgo;
	}
}

package edu.swe681.traverse.game;

import java.awt.Point;

/**
 * An immutable record of the last two Traverse moves.
 */
public final class MoveHistory
{
	private final Point oneMoveAgo;
	private final Point twoMoveAgo;
	private final Integer oneIDAgo;
	private final Integer twoIDAgo;

	public MoveHistory()
	{
		this(null, null, null, null);
	}
	
	public MoveHistory(Point oneMoveAgo, Integer oneIDAgo, Point twoMoveAgo, Integer twoIDAgo)
	{
		if (oneMoveAgo != null)
			this.oneMoveAgo = new Point(oneMoveAgo.x, oneMoveAgo.y);
		else
			this.oneMoveAgo = oneMoveAgo;
		this.oneIDAgo = oneIDAgo;
		
		if (twoMoveAgo != null)
			this.twoMoveAgo = new Point(twoMoveAgo.x, twoMoveAgo.y);
		else
			this.twoMoveAgo = twoMoveAgo;
		this.twoIDAgo = twoIDAgo;
	}

	public Point getOneMoveAgo()
	{
		if (oneMoveAgo == null)
			return null;
		else
			return new Point(oneMoveAgo.x, oneMoveAgo.y);
	}

	public Point getTwoMoveAgo()
	{
		if (twoMoveAgo == null)
			return null;
		else
			return new Point(twoMoveAgo.x, twoMoveAgo.y);
	}

	public Integer getOneIDAgo()
	{
		return oneIDAgo;
	}

	public Integer getTwoIDAgo()
	{
		return twoIDAgo;
	}
	
	public MoveHistory updateHistoryOne(Point oneMoveAgo, Integer oneIDAgo)
	{
		return new MoveHistory(oneMoveAgo, oneIDAgo, this.twoMoveAgo, this.twoIDAgo);
	}
	
	public MoveHistory updateHistoryTwo(Point twoMoveAgo, Integer twoIDAgo)
	{
		return new MoveHistory(this.oneMoveAgo, this.oneIDAgo, twoMoveAgo, twoIDAgo);
	}
}

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
	
	public MoveHistory(Integer oneMoveAgoX, Integer oneMoveAgoY, Integer oneIDAgo,
			Integer twoMoveAgoX, Integer twoMoveAgoY, Integer twoIDAgo)
	{
		if (oneMoveAgoX == null || oneMoveAgoY == null)
			this.oneMoveAgo = null;
		else
			this.oneMoveAgo = new Point(oneMoveAgoX, oneMoveAgoY);
		if (twoMoveAgoX == null || twoMoveAgoY == null)
			this.twoMoveAgo = null;
		else
			this.twoMoveAgo = new Point(twoMoveAgoX, twoMoveAgoY);
		
		this.oneIDAgo = oneIDAgo;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((oneIDAgo == null) ? 0 : oneIDAgo.hashCode());
		result = prime * result + ((oneMoveAgo == null) ? 0 : oneMoveAgo.hashCode());
		result = prime * result + ((twoIDAgo == null) ? 0 : twoIDAgo.hashCode());
		result = prime * result + ((twoMoveAgo == null) ? 0 : twoMoveAgo.hashCode());
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
		MoveHistory other = (MoveHistory) obj;
		if (oneIDAgo == null) {
			if (other.oneIDAgo != null)
				return false;
		} else if (!oneIDAgo.equals(other.oneIDAgo))
			return false;
		if (oneMoveAgo == null) {
			if (other.oneMoveAgo != null)
				return false;
		} else if (!oneMoveAgo.equals(other.oneMoveAgo))
			return false;
		if (twoIDAgo == null) {
			if (other.twoIDAgo != null)
				return false;
		} else if (!twoIDAgo.equals(other.twoIDAgo))
			return false;
		if (twoMoveAgo == null) {
			if (other.twoMoveAgo != null)
				return false;
		} else if (!twoMoveAgo.equals(other.twoMoveAgo))
			return false;
		return true;
	}
}

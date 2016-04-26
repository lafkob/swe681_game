package edu.swe681.traverse.rest.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import edu.swe681.traverse.game.GameBoard;

/**
 * Represents a single coordinate in the GameBoard that has validation rules.
 */
public class Coordinate {
	@NotNull
	@Min(0)
	@Max(GameBoard.SIZE - 1)
	private Integer x;
	
	@NotNull
	@Min(0)
	@Max(GameBoard.SIZE - 1)
	private Integer y;
	
	public Coordinate(){}
	
	public Coordinate(Integer x, Integer y) {
		this.x = x;
		this.y = y;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
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
		Coordinate other = (Coordinate) obj;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Move [x=" + x + ", y=" + y + "]";
	}
}

package edu.swe681.traverse.model;

import java.util.Date;

/**
 * POJO to represent the audit log model
 */
public class AuditModel implements Comparable<AuditModel>
{
	private long id;
	private long gameId;
	private Date timeStamp;
	private long playerId;
	private Integer pieceId;
	private String move;
	
	public AuditModel() {}
	
	public AuditModel(long id, long gameId, Date timeStamp, long playerId,
			Integer pieceId, String move) {
		super();
		this.id = id;
		this.gameId = gameId;
		this.timeStamp = new Date(timeStamp.getTime());
		this.playerId = playerId;
		this.pieceId = pieceId;
		this.move = move;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public Date getTimeStamp() {
		return new Date(timeStamp.getTime());
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = new Date(timeStamp.getTime());
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public Integer getPieceId() {
		return pieceId;
	}

	public void setPieceId(Integer pieceId) {
		this.pieceId = pieceId;
	}

	public String getMove() {
		return move;
	}

	public void setMove(String move) {
		this.move = move;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (gameId ^ (gameId >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((move == null) ? 0 : move.hashCode());
		result = prime * result + ((pieceId == null) ? 0 : pieceId.hashCode());
		result = prime * result + (int) (playerId ^ (playerId >>> 32));
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
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
		AuditModel other = (AuditModel) obj;
		if (gameId != other.gameId)
			return false;
		if (id != other.id)
			return false;
		if (move == null) {
			if (other.move != null)
				return false;
		} else if (!move.equals(other.move))
			return false;
		if (pieceId == null) {
			if (other.pieceId != null)
				return false;
		} else if (!pieceId.equals(other.pieceId))
			return false;
		if (playerId != other.playerId)
			return false;
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AuditModel [id=" + id + ", gameId=" + gameId + ", timeStamp=" + timeStamp + ", playerId=" + playerId
				+ ", pieceId=" + pieceId + ", move=" + move + "]";
	}

	/**
	 * Compares this object with the specified object for order.
	 * Comparisons of AuditModels are not consistent with equals.
	 * 
	 * @param otherModel The model to be compared
	 * 
	 * @return A negative integer, zero, or a positive integer as this object is
	 * 		less than, equal to, or greater than the specified model
	 */
	public int compareTo(AuditModel otherModel)
	{
		return this.timeStamp.compareTo(otherModel.timeStamp);
	}
}

package edu.swe681.traverse.model;

import java.sql.Timestamp;

/**
 * POJO to represent the audit log model
 */
public class AuditModel implements Comparable<AuditModel>
{
	private long id;
	private long gameId;
	private Timestamp timeStamp;
	private Long playerId;
	private int pieceId;
	private String move;
	
	public AuditModel() {}
	
	public AuditModel(long id, long gameId, Timestamp timeStamp, Long playerId,
			int pieceId, String move) {
		super();
		this.id = id;
		this.gameId = gameId;
		this.timeStamp = timeStamp;
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

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public int getPieceId() {
		return pieceId;
	}

	public void setPieceId(int pieceId) {
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
		result = prime * result + pieceId;
		result = prime * result + ((playerId == null) ? 0 : playerId.hashCode());
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
		if (pieceId != other.pieceId)
			return false;
		if (playerId == null) {
			if (other.playerId != null)
				return false;
		} else if (!playerId.equals(other.playerId))
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

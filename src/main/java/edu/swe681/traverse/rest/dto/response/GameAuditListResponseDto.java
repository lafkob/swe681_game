package edu.swe681.traverse.rest.dto.response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.swe681.traverse.model.AuditModel;

/**
 * Wraps a list of audit records for a specific game id that show the history of
 * moves in the game.
 */
public class GameAuditListResponseDto extends GameResponseDto {

	private List<AuditRecordResponseDto> gameAudit = new ArrayList<>();

	public GameAuditListResponseDto(long gameId, List<AuditModel> auditList) {
		super(gameId);
		if (auditList != null) {
			for(AuditModel model : auditList){
				gameAudit.add(new AuditRecordResponseDto(model));
			}
		}
	}

	public List<AuditRecordResponseDto> getGameAudit() {
		return gameAudit;
	}

	@Override
	public String toString() {
		return "GameAuditListResponseDto [gameAudit=" + gameAudit + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((gameAudit == null) ? 0 : gameAudit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameAuditListResponseDto other = (GameAuditListResponseDto) obj;
		if (gameAudit == null) {
			if (other.gameAudit != null)
				return false;
		} else if (!gameAudit.equals(other.gameAudit))
			return false;
		return true;
	}


	
	/**
	 * Encapsulates audit information that is sent back to the client.
	 */
	public static class AuditRecordResponseDto {
		private final String timestamp;
		private final Integer pieceId;
		private final long userId;
		private final String move;
		
		private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		public AuditRecordResponseDto(AuditModel model) {
			this.timestamp = DATE_FORMAT.format(model.getTimeStamp());
			this.pieceId = model.getPieceId();
			this.userId = model.getPlayerId();
			this.move = model.getMove();
		}

		public String getTimestamp() {
			return timestamp;
		}

		public Integer getPieceId() {
			return pieceId;
		}

		public long getUserId() {
			return userId;
		}

		public String getMove() {
			return move;
		}

		@Override
		public String toString() {
			return "AuditRecord [timestamp=" + timestamp + ", pieceId=" + pieceId + ", userId=" + userId + ", move="
					+ move + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((move == null) ? 0 : move.hashCode());
			result = prime * result + ((pieceId == null) ? 0 : pieceId.hashCode());
			result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
			result = prime * result + (int) (userId ^ (userId >>> 32));
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
			AuditRecordResponseDto other = (AuditRecordResponseDto) obj;
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
			if (timestamp == null) {
				if (other.timestamp != null)
					return false;
			} else if (!timestamp.equals(other.timestamp))
				return false;
			if (userId != other.userId)
				return false;
			return true;
		}
	}
}

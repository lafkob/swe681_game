package edu.swe681.traverse.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import edu.swe681.traverse.model.AuditModel;
import edu.swe681.traverse.utils.DaoUtils;

/**
 * Data access object for audit table
 */
@Service
public class AuditDao
{
	private final String CREATE = "INSERT INTO audit (GAME_ID, TIMESTAMP, PLAYER_ID, "
			+ "PIECE_ID, MOVE) VALUES (?, ?, ?, ?, ?)";
	private final String FIND_BY_GAME_ID = "SELECT ID, GAME_ID, TIMESTAMP, PLAYER_ID, "
			+ "PIECE_ID, MOVE FROM audit WHERE GAME_ID = ? ORDER BY TIMESTAMP DESC";
	
	private final JdbcTemplate jdbcTemplate;
	private final AuditRowMapper mapper;
	
	@Autowired
	public AuditDao(DataSource datasource)
	{
		Objects.requireNonNull(datasource, "datasource required");
		this.jdbcTemplate = new JdbcTemplate(datasource);
		this.mapper = new AuditRowMapper();
	}
	
	public void addAuditLine(long gameId, Date timeStamp, long playerId,
			Integer pieceId, String move)
	{
		jdbcTemplate.update(CREATE, gameId, timeStamp, playerId, pieceId, move);
	}
	
	public List<AuditModel> getAuditsByGameId(String gameId)
	{
		return jdbcTemplate.query(FIND_BY_GAME_ID, mapper, gameId);
	}
	
	/**
	 * Maps a ResultSet row from the audit table to an AuditModel
	 */
	private static class AuditRowMapper implements RowMapper<AuditModel>
	{
		private final static String ID_COL = "ID";
		private final static String GAME_ID_COL = "GAME_ID";
		private final static String TIMESTAMP_COL = "TIMESTAMP";
		private final static String PLAYER_ID_COL = "PLAYER_ID";
		private final static String PIECE_ID_COL = "PIECE_ID";
		private final static String MOVE_COL = "MOVE";

		@Override
		public AuditModel mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			return new AuditModel(rs.getLong(ID_COL), rs.getLong(GAME_ID_COL), 
					rs.getDate(TIMESTAMP_COL), rs.getLong(PLAYER_ID_COL),
					DaoUtils.getInteger(rs, PIECE_ID_COL), rs.getString(MOVE_COL));
		}
	}
}

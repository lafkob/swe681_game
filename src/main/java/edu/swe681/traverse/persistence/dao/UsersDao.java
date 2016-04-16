package edu.swe681.traverse.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import edu.swe681.traverse.model.UserModel;

/**
 * Data access object for the users table.
 */
public class UsersDao {

	private final String CREATE = "INSERT INTO USERS (USERNAME, PASSWORD_HASH, PASSWORD_SALT) VALUES (?, ?, ?)";
	private final String FIND_BY_USERNAME = "SELECT ID, USERNAME, PASSWORD_HASH, PASSWORD_SALT FROM USERS WHERE USERNAME = ?";

	private final JdbcTemplate jdbcTemplate;
	private final UsersRowMapper mapper;

	@Autowired
	public UsersDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = Objects.requireNonNull(jdbcTemplate, "jdbcTemplate required");
		this.mapper = new UsersRowMapper();
	}

	public void saveUser(String username, String passwordHash, String passwordSalt) {
		jdbcTemplate.update(CREATE, username, passwordHash, passwordSalt);
	}

	public UserModel getUserByUsername(String username) {
		return jdbcTemplate.queryForObject(FIND_BY_USERNAME, mapper, username);
	}

	/**
	 * Maps a ResultSet row from the users table to a UserModel
	 */
	private static class UsersRowMapper implements RowMapper<UserModel> {
		private final static String ID_COL = "ID";
		private final static String USERNAME_COL = "USERNAME";
		private final static String PASSWORD_HASH_COL = "PASSWORD_HASH";
		private final static String PASSWORD_SALT_COL = "PASSWORD_SALT";

		@Override
		public UserModel mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new UserModel(rs.getLong(ID_COL), rs.getString(USERNAME_COL), 
					rs.getString(PASSWORD_HASH_COL), rs.getString(PASSWORD_SALT_COL));
		}

	}
}
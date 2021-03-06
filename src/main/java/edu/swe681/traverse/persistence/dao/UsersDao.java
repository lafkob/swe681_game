package edu.swe681.traverse.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.swe681.traverse.model.UserModel;

/**
 * Data access object for the users table.
 */
@Service
public class UsersDao {

	private static final String CREATE = "INSERT INTO USERS (USERNAME, PASSWORD_HASH, AUTH_ATTEMPTS, ENABLED) VALUES (?, ?, 0, true)";
	private static final String CREATE_ROLE = "INSERT INTO user_roles (username, role) VALUES(?, 'ROLE_USER')";
	private static final String FIND_BY_USERNAME = "SELECT ID, USERNAME, PASSWORD_HASH, AUTH_ATTEMPTS FROM USERS WHERE USERNAME = ?";
	private static final String DISABLE_USER = "UPDATE USERS SET ENABLED=0 WHERE USERNAME=?";
	private static final String INCREMENT_AUTH_ATTEMPTS = "UPDATE USERS SET AUTH_ATTEMPTS=AUTH_ATTEMPTS+1 WHERE USERNAME=?";
	private static final String RESET_AUTH_ATTEMPTS = "UPDATE USERS SET AUTH_ATTEMPTS=0 WHERE USERNAME=?";
	
	private final JdbcTemplate jdbcTemplate;
	private final UsersRowMapper mapper;
	
	@Autowired
	public UsersDao(DataSource datasource) {
		Objects.requireNonNull(datasource, "datasource required");
		this.jdbcTemplate = new JdbcTemplate(datasource);
		this.mapper = new UsersRowMapper();
	}

	@Transactional // because two queries
	public void saveUser(String username, String passwordHash) {
		jdbcTemplate.update(CREATE, username, passwordHash);
		jdbcTemplate.update(CREATE_ROLE, username);
	}
	
	public boolean doesUsernameExist(String username) {
		return jdbcTemplate.query(FIND_BY_USERNAME, mapper, username).size() > 0;
	}

	public UserModel getUserByUsername(String username) {
		return jdbcTemplate.queryForObject(FIND_BY_USERNAME, mapper, username);
	}
	
	public void disableUser(String username)
	{
		jdbcTemplate.update(DISABLE_USER, username);
	}
	
	public void incrementAuthAttempts(String username)
	{
		jdbcTemplate.update(INCREMENT_AUTH_ATTEMPTS, username);
	}
	
	public void resetAuthAttempts(String username)
	{
		jdbcTemplate.update(RESET_AUTH_ATTEMPTS, username);
	}

	/**
	 * Maps a ResultSet row from the users table to a UserModel
	 */
	private static class UsersRowMapper implements RowMapper<UserModel> {
		private final static String ID_COL = "ID";
		private final static String USERNAME_COL = "USERNAME";
		private final static String PASSWORD_HASH_COL = "PASSWORD_HASH";
		private final static String AUTH_ATTEMPTS_COL = "AUTH_ATTEMPTS";

		@Override
		public UserModel mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new UserModel(rs.getLong(ID_COL), rs.getString(USERNAME_COL), 
					rs.getString(PASSWORD_HASH_COL), rs.getInt(AUTH_ATTEMPTS_COL));
		}

	}
}

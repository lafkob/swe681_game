package edu.swe681.traverse.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import edu.swe681.traverse.model.UserModel;

/**
 * Data access object for the user table.
 */
public class UserDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void saveUser(UserModel model) {
		// TODO
	}
	
	public UserModel getUserByUsername(String username) {
		// TODO
		return null;
	}
}

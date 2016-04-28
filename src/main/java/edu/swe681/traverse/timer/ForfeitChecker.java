package edu.swe681.traverse.timer;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Objects;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import edu.swe681.traverse.game.enums.GameStatus;

/**
 * A timer class that will run and automatically forfeit a user if they have
 * taken too long to make a move in their game.
 */
@Component
public class ForfeitChecker {
	private static final Logger LOG = LoggerFactory.getLogger(ForfeitChecker.class);
	
	/**
	 * Conditional select to get the player who is NOT the current player.
	 */
	private final static String SELECT_NON_CURRENT_PLAYER = 
			"SELECT CASE WHEN PLAYER1_ID = CURRENT_PLAYER_ID THEN PLAYER2_ID ELSE PLAYER1_ID END as WINNER_ID";
	
	private final static String SELECT_EXPIRED_GAME_IDS = 
			"select game_id from (select max(game_id) as game_id, max(timestamp) from audit a where a.timestamp < ? group by a.game_id) as game_id";
	
	private final static String FORFEIT_EXPIRED_GAMES = 
			"UPDATE GAMES SET CURRENT_PLAYER_ID = ("+ SELECT_NON_CURRENT_PLAYER + "), STATUS = '" + GameStatus.FORFEIT +
			"' WHERE ID IN (" + SELECT_EXPIRED_GAME_IDS + ") AND STATUS = '" + GameStatus.PLAY + "'";
	
	private final static int TIMEOUT_MINUTES = 5;
	
	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public ForfeitChecker(DataSource datasource)
	{
		Objects.requireNonNull(datasource, "datasource required");
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}
	
	/**
	 * Forfeit process method. Fires on startup and every 5 minutes after that.
	 */
	@Scheduled(fixedRate = 300000)
	public void processForfeits() {
		LOG.info("Running forfeit process job");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 0 - TIMEOUT_MINUTES);
		
		int updated = jdbcTemplate.update(FORFEIT_EXPIRED_GAMES, new Timestamp(cal.getTimeInMillis()));
		LOG.info("Forfeited " + updated + " games");
		
		// TODO: remove
//		System.out.println("Forfeited " + updated + " games");
	}
}

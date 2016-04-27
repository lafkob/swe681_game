package edu.swe681.traverse.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import edu.swe681.traverse.game.enums.GameStatus;
import edu.swe681.traverse.model.GameModel;
import edu.swe681.traverse.rest.dto.response.UserInfoListResponseDto.UserInfoResponseDto;
import edu.swe681.traverse.utils.DaoUtils;

/**
 * Data access object for the games table.
 */
@Service
public class GamesDao {

	private final static String SELECT = "SELECT ID, BOARD, STATUS, PLAYER1_ID, PLAYER2_ID, CURRENT_PLAYER_ID, " +
			"P1_ONE_MOVE_AGO_X, P1_ONE_MOVE_AGO_Y, P1_TWO_MOVE_AGO_X, P1_TWO_MOVE_AGO_Y, P1_ONE_ID_AGO, P1_TWO_ID_AGO, " +
			"P2_ONE_MOVE_AGO_X, P2_ONE_MOVE_AGO_Y, P2_TWO_MOVE_AGO_X, P2_TWO_MOVE_AGO_Y, P2_ONE_ID_AGO, P2_TWO_ID_AGO " +
			"FROM GAMES ";
	private final static String FIND_BY_ID = SELECT + "WHERE ID = ?";
	
	private final static String CREATE_GAME = "INSERT INTO GAMES (PLAYER1_ID, STATUS) VALUES (?, ?)";
	
	private final static String UPDATE_GAME = "UPDATE GAMES SET BOARD=?, STATUS=?, PLAYER1_ID=?, PLAYER2_ID=?, CURRENT_PLAYER_ID=?, "
			+ "P1_ONE_MOVE_AGO_X=?, P1_ONE_MOVE_AGO_Y=?, P1_TWO_MOVE_AGO_X=?, P1_TWO_MOVE_AGO_Y=?, P1_ONE_ID_AGO=?, P1_TWO_ID_AGO=?, "
			+ "P2_ONE_MOVE_AGO_X=?, P2_ONE_MOVE_AGO_Y=?, P2_TWO_MOVE_AGO_X=?, P2_TWO_MOVE_AGO_Y=?, P2_ONE_ID_AGO=?, P2_TWO_ID_AGO=? "
			+ "WHERE ID = ?";
	
	// some building blocks for queries
	private final static String RUNNING_GAME_FILTER = "(STATUS = '" + GameStatus.WAITING_FOR_PLAYER_TWO + "' OR STATUS = '" + GameStatus.PLAY + "')";
	private final static String FINISHED_GAME_FILTER = "(STATUS = '" + GameStatus.WIN + "' OR STATUS = '" + GameStatus.FORFEIT + "')";
	
	
	private final static String PLAYER_CURRENT_GAME_COUNT = 
			"SELECT COUNT(*) as COUNT FROM GAMES WHERE (PLAYER1_ID = ? OR PLAYER2_ID = ?) AND " + RUNNING_GAME_FILTER;
	
	private final static String PLAYER_WIN_COUNT = 
			"SELECT COUNT(*) as WINS FROM GAMES WHERE (PLAYER1_ID = ? OR PLAYER2_ID = ?) AND CURRENT_PLAYER_ID = ? AND " + FINISHED_GAME_FILTER;
	
	private final static String PLAYER_LOSS_COUNT = 
			"SELECT COUNT(*) as LOSSES FROM GAMES WHERE (PLAYER1_ID = ? OR PLAYER2_ID = ?) AND CURRENT_PLAYER_ID != ? AND " + FINISHED_GAME_FILTER;
	
	
	private final static String OPEN_GAME_IDS = "SELECT ID FROM GAMES WHERE STATUS = '" + GameStatus.WAITING_FOR_PLAYER_TWO + "'";
	private final static String FINISHED_GAME_IDS = "SELECT ID FROM GAMES WHERE " + FINISHED_GAME_FILTER;
	
	/**
	 * Makes heavy reuse of other query building blocks, requires 6 string parameters, all the same value: "usr.ID"
	 * 
	 * We want:
	 * SELECT usr.username, 
	 * (SELECT COUNT(*) as WINS 
	 * 		FROM GAMES 
	 * 		WHERE (PLAYER1_ID = usr.ID OR PLAYER2_ID = usr.ID) AND CURRENT_PLAYER_ID = usr.ID AND (STATUS = 'WIN' OR STATUS='FORFEIT')) as WINS, 
	 * (SELECT COUNT(*) as LOSSES 
	 * 		FROM GAMES 
	 * 		WHERE (PLAYER1_ID = usr.ID OR PLAYER2_ID = usr.ID) AND CURRENT_PLAYER_ID != usr.ID AND (STATUS = 'WIN' OR STATUS='FORFEIT')) as LOSSES 
	 * FROM USERS usr;
	 */
	private final static String SELECT_USER_INFO = 
			"SELECT usr.username, (" + PLAYER_WIN_COUNT + ") as WINS, (" + PLAYER_LOSS_COUNT + ") as LOSSES FROM USERS usr";
	
	
	private final JdbcTemplate jdbcTemplate;
	private final GamesRowMapper gameMapper;
	private final UserInfoMapper userInfoMapper;
	
	@Autowired
	public GamesDao(DataSource datasource) {
		Objects.requireNonNull(datasource, "dataSource required");
		this.jdbcTemplate = new JdbcTemplate(datasource);
		gameMapper = new GamesRowMapper();
		userInfoMapper = new UserInfoMapper();
	}
	
	/**
	 * Checks to see if the given user is already involved in any active games.
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isPlayerCurrentlyInAGame(long userId) {
		int gameCount = jdbcTemplate.queryForObject(PLAYER_CURRENT_GAME_COUNT, Integer.class, userId, userId);
		return gameCount > 0;
	}
	
	/**
	 * Creates a new game in the database with the default status of WAITING_FOR_PLAYERS.
	 * Note: this does not limit the number of games a player can be in.
	 * 
	 * @param playerId Id of the user who is starting the game
	 * @return Id of the newly created game
	 */
	public long startNewGame(long playerId) {
		KeyHolder holder = new GeneratedKeyHolder();
		PreparedStatementCreator psc = new CreateGamePreparedStatementCreator(playerId, GameStatus.WAITING_FOR_PLAYER_TWO.toString());
		jdbcTemplate.update(psc, holder);
		return holder.getKey().longValue();
	}
	
	/**
	 * Fetches the game data for the given game id.
	 * 
	 * @param gameId ID of the game to get
	 * @return GameModel representing the row
	 */
	public GameModel getGameById(long gameId) {
		List<GameModel> matches = jdbcTemplate.query(FIND_BY_ID, gameMapper, gameId);
		if(matches.size() == 0) return null;
		else return matches.get(0); // shouldn't be finding more than one for a game id!
	}
	
	/**
	 * Updates the row for the given GameModel.
	 * 
	 * @param m GameModel representing a row in the games table.
	 */
	public void updateGame(GameModel m) {
		jdbcTemplate.update(UPDATE_GAME, m.getBoard(), m.getGameStatus().toString(), m.getPlayerOneId(),
				m.getPlayerTwoId(), m.getCurrentPlayerId(), m.getP1OneMoveAgoX(), m.getP1OneMoveAgoY(),
				m.getP1TwoMoveAgoX(), m.getP1TwoMoveAgoY(), m.getP1OneIdAgo(), m.getP1TwoIdAgo(), m.getP2OneMoveAgoX(),
				m.getP2OneMoveAgoY(), m.getP2TwoMoveAgoX(), m.getP2TwoMoveAgoY(), m.getP2OneIdAgo(), m.getP2TwoIdAgo(),
				m.getGameId());
	}
	
	/**
	 * Returns the info for all users in the system: username + W-L record
	 * 
	 * @return UserInfoResponseDto objects for all users
	 */
	public List<UserInfoResponseDto> getAllUserInfo() {
		// since this query heavily reuses other building blocks, we have to stick these in all parameters
		// so that the joins are done properly
		Object[] params = {"usr.ID", "usr.ID", "usr.ID", "usr.ID", "usr.ID", "usr.ID"};
		return jdbcTemplate.query(SELECT_USER_INFO, userInfoMapper, params);
	}
	
	/**
	 * Gets the list of game ids in which there is only one player, waiting for a second player to join.
	 * 
	 * @return List of game ids
	 */
	public List<Long> getOpenGameIds() {
		return jdbcTemplate.queryForList(OPEN_GAME_IDS, Long.class);
	}
	
	/**
	 * Gets the list of game ids for finished games (WIN or FORFEIT status).
	 * 
	 * @return List of game ids
	 */
	public List<Long> getFinishedGameIds() {
		return jdbcTemplate.queryForList(FINISHED_GAME_IDS, Long.class);
	}
	
	/**
	 * Maps a ResultSet row from the games table to a GameModel
	 */
	private static class GamesRowMapper implements RowMapper<GameModel> {

		private final static String ID_COL = "ID";
		private final static String BOARD_COL = "BOARD";
		private final static String STATUS_COL = "STATUS";
		private final static String PLAYER1_ID_COL = "PLAYER1_ID";
		private final static String PLAYER2_ID_COL = "PLAYER2_ID";
		private final static String CURRENT_PLAYER_ID_COL = "CURRENT_PLAYER_ID";
		private final static String P1_ONE_MOVE_AGO_X_COL = "P1_ONE_MOVE_AGO_X";
		private final static String P1_ONE_MOVE_AGO_Y_COL = "P1_ONE_MOVE_AGO_Y";
		private final static String P1_TWO_MOVE_AGO_X_COL = "P1_TWO_MOVE_AGO_X";
		private final static String P1_TWO_MOVE_AGO_Y_COL = "P1_TWO_MOVE_AGO_Y";
		private final static String P1_ONE_ID_AGO_COL = "P1_ONE_ID_AGO";
		private final static String P1_TWO_ID_AGO_COL = "P1_TWO_ID_AGO";
		private final static String P2_ONE_MOVE_AGO_X_COL = "P2_ONE_MOVE_AGO_X";
		private final static String P2_ONE_MOVE_AGO_Y_COL = "P2_ONE_MOVE_AGO_Y";
		private final static String P2_TWO_MOVE_AGO_X_COL = "P2_TWO_MOVE_AGO_X";
		private final static String P2_TWO_MOVE_AGO_Y_COL = "P2_TWO_MOVE_AGO_Y";
		private final static String P2_ONE_ID_AGO_COL = "P2_ONE_ID_AGO";
		private final static String P2_TWO_ID_AGO_COL = "P2_TWO_ID_AGO";

		@Override
		public GameModel mapRow(ResultSet rs, int rowNum) throws SQLException {
			GameModel model = new GameModel();
			model.setGameId(rs.getLong(ID_COL));
			model.setBoard(rs.getString(BOARD_COL));
			model.setGameStatus(GameStatus.valueOf(rs.getString(STATUS_COL)));
			model.setPlayerOneId(rs.getLong(PLAYER1_ID_COL));
			model.setPlayerTwoId(DaoUtils.getLong(rs, PLAYER2_ID_COL));
			model.setCurrentPlayerId(DaoUtils.getLong(rs, CURRENT_PLAYER_ID_COL));
			model.setP1OneMoveAgoX(DaoUtils.getInteger(rs, P1_ONE_MOVE_AGO_X_COL));
			model.setP1OneMoveAgoY(DaoUtils.getInteger(rs, P1_ONE_MOVE_AGO_Y_COL));
			model.setP1TwoMoveAgoX(DaoUtils.getInteger(rs, P1_TWO_MOVE_AGO_X_COL));
			model.setP1TwoMoveAgoY(DaoUtils.getInteger(rs, P1_TWO_MOVE_AGO_Y_COL));
			model.setP1OneIdAgo(DaoUtils.getInteger(rs, P1_ONE_ID_AGO_COL));
			model.setP1TwoIdAgo(DaoUtils.getInteger(rs, P1_TWO_ID_AGO_COL));
			model.setP2OneMoveAgoX(DaoUtils.getInteger(rs, P2_ONE_MOVE_AGO_X_COL));
			model.setP2OneMoveAgoY(DaoUtils.getInteger(rs, P2_ONE_MOVE_AGO_Y_COL));
			model.setP2TwoMoveAgoX(DaoUtils.getInteger(rs, P2_TWO_MOVE_AGO_X_COL));
			model.setP2TwoMoveAgoY(DaoUtils.getInteger(rs, P2_TWO_MOVE_AGO_Y_COL));
			model.setP2OneIdAgo(DaoUtils.getInteger(rs, P2_ONE_ID_AGO_COL));
			model.setP2TwoIdAgo(DaoUtils.getInteger(rs, P2_TWO_ID_AGO_COL));
			
			return model;
		}
		
	}
	
	/**
	 * Maps a ResultSet row directly to a UserInfoResponseDto
	 */
	private static class UserInfoMapper implements RowMapper<UserInfoResponseDto> {

		private final static String USERNAME_COL = "USERNAME";
		private final static String WINS_COL = "WINS";
		private final static String LOSSES_COL = "LOSSES";

		@Override
		public UserInfoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new UserInfoResponseDto(rs.getString(USERNAME_COL), DaoUtils.getInteger(rs, WINS_COL), DaoUtils.getInteger(rs, LOSSES_COL));
		}
		
	}
	
	/**
	 * PreparedStatementCreator implementation for creating a new game, where we need
	 * to immediately get back the ID assigned to the game.
	 */
	private static class CreateGamePreparedStatementCreator implements PreparedStatementCreator {
		private final long playerId;
		private final String status;
		
		public CreateGamePreparedStatementCreator(long playerId, String status){
			this.playerId = playerId;
			this.status = status;
		}
		
		@Override
		public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
			PreparedStatement ps = conn.prepareStatement(CREATE_GAME, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, this.playerId);
			ps.setString(2, status);
			return ps;
		}
	}
}

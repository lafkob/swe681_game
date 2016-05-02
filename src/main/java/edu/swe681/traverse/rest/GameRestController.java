package edu.swe681.traverse.rest;

import java.awt.Point;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.swe681.traverse.application.exception.BadRequestException;
import edu.swe681.traverse.application.exception.InternalServerException;
import edu.swe681.traverse.application.exception.NotFoundException;
import edu.swe681.traverse.game.GameBoard;
import edu.swe681.traverse.game.exception.TraverseException;
import edu.swe681.traverse.model.GameModel;
import edu.swe681.traverse.model.UserModel;
import edu.swe681.traverse.persistence.dao.AuditDao;
import edu.swe681.traverse.persistence.dao.GamesDao;
import edu.swe681.traverse.persistence.dao.UsersDao;
import edu.swe681.traverse.rest.dto.Coordinate;
import edu.swe681.traverse.rest.dto.request.GameRequestDto;
import edu.swe681.traverse.rest.dto.request.MoveRequestDto;
import edu.swe681.traverse.rest.dto.response.GameResponseDto;
import edu.swe681.traverse.rest.dto.response.GameStatusResponseDto;

/**
 * REST controller for the game API.
 */
@RestController
@RequestMapping(value="/api/game")
public class GameRestController {

	private static final Logger LOG = LoggerFactory.getLogger(GameRestController.class);
	
	@Autowired
	private GamesDao gamesDao;
	
	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private AuditDao auditDao;
	
	/**
	 * Starts a new game. Player will be the only player in the game. Player
	 * must not already be in another active game.
	 * 
	 * @param principal User's principal, Spring will auto populate this
	 * @return Response with the new gameId
	 * @throws BadRequestException
	 * @throws InternalServerException 
	 */
	@RequestMapping(value="/start", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public GameResponseDto startNewGame(Principal principal) throws BadRequestException, InternalServerException {
		final long userId = usersDao.getUserByUsername(principal.getName()).getId();
		if(gamesDao.isPlayerCurrentlyInAGame(userId)) {
			LOG.info("Attempt to start a game while in a game. User: " + principal.getName());
			throw new BadRequestException("User is already in a game.");
		}
		
		final long gameId = gamesDao.startNewGame(userId);
		// update the board now by creating the object and then writing it,
		// this will set all the initial internal states up properly!
		GameBoard board = new GameBoard(gameId, userId, true);
		writeBoardToDatabase(board);
		LOG.info(String.format("User %s started game %d", principal.getName(), gameId));
		LOG.info(String.format("Starting line configurations for game %d are - %s", gameId,
				startingLineLayoutToString(board)));
		auditDao.addAuditLine(gameId, new Date(), userId, null, principal.getName() + " started game.");
		auditDao.addAuditLine(gameId, new Date(), userId, null, "Starting line configurations - " +
				startingLineLayoutToString(board));
		return new GameResponseDto(gameId);
	}
	
	
	/**
	 * Quits the player from the game specified by the gameId. Player
	 * must be in the game already. Either forfeits them or kills a
	 * game that is waiting to start.
	 * 
	 * @param dto Request containing the gameId
	 * @param principal User's principal, Spring will auto populate this
	 * @return 200
	 * @throws NotFoundException 
	 * @throws InternalServerException 
	 * @throws TraverseException 
	 */
	@RequestMapping(value="/quit", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public ResponseEntity<String> quitGame(@Valid @RequestBody GameRequestDto dto, Principal principal)
			throws NotFoundException, InternalServerException, TraverseException {
		final GameModel game = gamesDao.getGameById(dto.getGameId());
		final UserModel user = usersDao.getUserByUsername(principal.getName());
		
		validateGameAndUserInGame(game, user, "/quit");
		
		GameBoard board = gameModelToGameBoard(game);
		board = board.playerQuit(user.getId());
		writeBoardToDatabase(board);
		LOG.info(String.format("User %s quit game %d", principal.getName(), dto.getGameId()));
		auditDao.addAuditLine(dto.getGameId(), new Date(), user.getId(), null, principal.getName() + " quit game.");
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	/**
	 * Joins a player to a game specified by the gameId. The game must exist and
	 * the player not already be in a game. Will trigger the game to start.
	 * 
	 * @param dto Request containing the gameId
	 * @param principal User's principal, Spring will auto populate this
	 * @return 200 with a GameStatusResponse JSON object
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws InternalServerException
	 * @throws TraverseException
	 */
	@RequestMapping(value="/join", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public GameStatusResponseDto joinGame(@Valid @RequestBody GameRequestDto dto, Principal principal)
			throws BadRequestException, NotFoundException, InternalServerException, TraverseException {
		final GameModel game = gamesDao.getGameById(dto.getGameId());
		
		if(game == null) {
			throw new NotFoundException("Game not found");
		}
		
		final long userId = usersDao.getUserByUsername(principal.getName()).getId();
		
		// make sure user is not already in a game
		if (gamesDao.isPlayerCurrentlyInAGame(userId)) {
			LOG.info("Attempt to join a game while in a game. User: " + principal.getName() + " GameID (join): "
					+ dto.getGameId());
			throw new BadRequestException("User already in a game");
		}
		
		GameBoard board = gameModelToGameBoard(game);
		board = board.registerPlayerTwo(userId);
		writeBoardToDatabase(board);
		LOG.info(String.format("User %s joined game %d", principal.getName(), dto.getGameId()));
		auditDao.addAuditLine(dto.getGameId(), new Date(), userId, null, principal.getName() + " joined game.");
		
		return new GameStatusResponseDto(dto.getGameId(), board.getBoard(), board.getGameState().getCurrentPlayerID(),
				board.getPlayerOneID(), board.getPlayerTwoID(), board.getGameState().getStatus());
	}

	/**
	 * Gets the status of the game specified by the gameId. Game must exist and 
	 * the requesting player must be a participant in the game.
	 * 
	 * @param dto Request containing the gameId
	 * @param principal User's principal, Spring will auto populate this
	 * @return 200 with a GameStatusResponse JSON object
	 * @throws NotFoundException
	 * @throws InternalServerException
	 */
	@RequestMapping(value="/status", method = RequestMethod.POST)
	@ResponseBody
	public GameStatusResponseDto getStatus(@Valid @RequestBody GameRequestDto dto, Principal principal)
			throws NotFoundException, InternalServerException {
		final GameModel game = gamesDao.getGameById(dto.getGameId());
		final UserModel user = usersDao.getUserByUsername(principal.getName());
		
		validateGameAndUserInGame(game, user, "/status");
		
		int[][] board;
		try {
			board = game.boardAsArray();
		} catch (IOException e) {
			LOG.error("Error while converting game board to array! GameID: " + game.getGameId(), e);
			throw new InternalServerException("Bad game state, please contact an admin");
		}
		return new GameStatusResponseDto(dto.getGameId(), board, game.getCurrentPlayerId(),
				game.getPlayerOneId(), game.getPlayerTwoId(), game.getGameStatus());
	}
	

	/**
	 * Moves a piece on a game. Game must exist and the requesting player must
	 * be a participant in the game. It must be the player's turn. Move must be
	 * valid by the rules of traverse.
	 * 
	 * @param dto Request containing the gameId
	 * @param principal User's principal, Spring will auto populate this
	 * @return 200 with a GameStatusResponse JSON object
	 * @throws NotFoundException
	 * @throws InternalServerException
	 * @throws TraverseException
	 * @throws BadRequestException
	 */
	@RequestMapping(value="/move", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public GameStatusResponseDto makeMove(@Valid @RequestBody MoveRequestDto dto, Principal principal)
			throws NotFoundException, InternalServerException, TraverseException, BadRequestException {
		final GameModel game = gamesDao.getGameById(dto.getGameId());
		final UserModel user = usersDao.getUserByUsername(principal.getName());
		
		validateGameAndUserInGame(game, user, "/move");
		GameBoard board = gameModelToGameBoard(game);
		
		// make sure it is this players turn!
		if(user.getId() != board.getGameState().getCurrentPlayerID()){
			throw new BadRequestException("It is not the requested user's turn.");
		}
		
		board = board.movePiece(dto.getPieceId(), convertCoordinatesToPoints(dto.getMoves()));
		writeBoardToDatabase(board);
		LOG.info(String.format("Game %d, user %s: Move made, piece %d to %s", dto.getGameId(), principal.getName(),
				dto.getPieceId(), coordinatesToString(dto.getMoves())));
		auditDao.addAuditLine(dto.getGameId(), new Date(), user.getId(), dto.getPieceId(), coordinatesToString(dto.getMoves()));
		
		return new GameStatusResponseDto(dto.getGameId(), board.getBoard(), board.getGameState().getCurrentPlayerID(),
				board.getPlayerOneID(), board.getPlayerTwoID(), board.getGameState().getStatus());
	}

	
	/**
	 * Wraps the logic for converting a GameModel to a GameBoard, including the exception handling.
	 * 
	 * @param model GameModel to convert
	 * @return Converted GameBoard
	 * @throws InternalServerException Serde issues with the int[][] board
	 */
	private GameBoard gameModelToGameBoard(GameModel model) throws InternalServerException {
		try {
			return new GameBoard(model);
		} catch (IOException e) {
			LOG.error("Error while converting GameModel to a GameBoard! GameID: " + model.getGameId(), e);
			throw new InternalServerException("Bad game state, please contact an admin");
		}
	}

	/**
	 * Wraps the logic for writing a GameBoard into the database, including the
	 * conversion to a GameModel and exception handling.
	 * 
	 * @param board GameBoard to write to the database
	 * @throws InternalServerException Serde issues with the int[][] board
	 */
	private void writeBoardToDatabase(GameBoard board) throws InternalServerException {
		try {
			gamesDao.updateGame(new GameModel(board));
		} catch (JsonProcessingException e) {
			LOG.error("Error while writing GameModel to the database! GameID: " + board.getGameID(), e);
			throw new InternalServerException("Bad game state, please contact an admin");
		}
	}
	
	/**
	 * Checks to ensure that the game is not null and the the given user is a participant in the game.
	 * 
	 * @param game Game to check
	 * @param user User to check
	 * @return True if the game is not null and the user is a participant in the game
	 * @throws NotFoundException Vague exception with message about game not found if it is null or the user is not a participant
	 */
	private boolean validateGameAndUserInGame(GameModel game, UserModel user, String method) throws NotFoundException {
		// make sure the user is in the game, don't give a specific error as
		// to whether the game exists
		if (game == null || !game.isUserInGame(user.getId())) {
			String gameIdStr = game == null ? "null" : Long.toString(game.getGameId());
			
			LOG.info("Attempt to operate on non-existent game or game in which user is not a participant. User: "
					+ user.getUsername() + " GameID: " + gameIdStr + " Method: " + method);
			throw new NotFoundException("No game found for user");
		}
		
		return true;
	}
	
	/**
	 * Converter from Coordinate model objects into the Point objects used by
	 * the GameBoard.
	 * 
	 * @param coords Coordinate objects to convert
	 * @return Point objects or null if coords is null
	 */
	private List<Point> convertCoordinatesToPoints(List<Coordinate> coords) {
		if(coords == null) return null;
		
		List<Point> points = new ArrayList<>(coords.size());
		if(!coords.isEmpty()) {
			for(Coordinate coord : coords) {
				points.add(new Point(coord.getX(), coord.getY()));
			}
		}
		return points;
	}
	
	/**
	 * Converts a list of Coordinates into a string suitable for auditing moves.
	 * The game engine limits the number of possible coordinates to 10.
	 * 
	 * @param coords Coordinate objects to convert
	 * @return String representation of the objects or null if coords is null
	 */
	private String coordinatesToString(List<Coordinate> coords) {
		if(coords == null) return null;
		
		StringBuilder builder = new StringBuilder();
		if(!coords.isEmpty()) {
			// append the first one, no commas
			builder.append("(").append(coords.get(0).getX()).append(",").append(coords.get(0).getY()).append(")");
			
			// append the rest, prefix with a comma
			for(int i = 1; i < coords.size(); i++) {
				builder.append(",(").append(coords.get(i).getX()).append(",").append(coords.get(i).getY()).append(")");
			}
		}
		
		return builder.toString();
	}
	
	private String startingLineLayoutToString(GameBoard board)
	{
		StringBuilder builder = new StringBuilder();
		int[][] tempBoard = board.getBoard();
		
		/* Player 1 row */
		builder.append("Player 1: [");
		/* Append the first one, no space */
		builder.append(tempBoard[0][1]);
		for (int i = 2; i < GameBoard.SIZE-1; i++)
		{
			builder.append(" ").append(tempBoard[0][i]);
		}
		
		/* Player 2 row */
		builder.append("], Player 2: [");
		/* Append the first one, no space */
		builder.append(tempBoard[GameBoard.SIZE-1][1]);
		for (int i = 2; i < GameBoard.SIZE-1; i++)
		{
			builder.append(" ").append(tempBoard[GameBoard.SIZE-1][i]);
		}
		builder.append("]");
		
		return builder.toString();
	}
}

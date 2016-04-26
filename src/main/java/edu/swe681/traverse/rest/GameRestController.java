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
import edu.swe681.traverse.rest.dto.request.JoinRequestDto;
import edu.swe681.traverse.rest.dto.request.MoveRequestDto;
import edu.swe681.traverse.rest.dto.request.StatusRequestDto;
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
	 * Starts a new game. Player will be the only player in the game.
	 * 
	 * @return Response with the new gameId
	 * @throws BadRequestException 
	 */
	@RequestMapping(value="/start", method = RequestMethod.POST)
	@ResponseBody
	public GameResponseDto startNewGame(Principal principal) throws BadRequestException {
		final long userId = usersDao.getUserByUsername(principal.getName()).getId();
		if(gamesDao.isPlayerCurrentlyInAGame(userId)) {
			LOG.info("Attempt to start a game while in a game. User: " + principal.getName());
			throw new BadRequestException("User is already in a game.");
		}
		
		final long gameId = gamesDao.startNewGame(userId);
		auditDao.addAuditLine(gameId, new Date(), userId, null, principal.getName() + " started game.");
		return new GameResponseDto(gameId);
	}
	
	
	/**
	 * Quits the player from the game specified by the gameId. Player
	 * must be in the game already. Either forfeits them or kills a
	 * game that is waiting to start.
	 * 
	 * @param dto Request containing the gameId
	 * @return 200
	 * @throws NotFoundException 
	 * @throws InternalServerException 
	 * @throws TraverseException 
	 */
	@RequestMapping(value="/quit", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> quitGame(@Valid @RequestBody GameRequestDto dto, Principal principal)
			throws NotFoundException, InternalServerException, TraverseException {
		final GameModel game = gamesDao.getGameById(dto.getGameId());
		final UserModel user = usersDao.getUserByUsername(principal.getName());
		
		validateGameAndUserInGame(game, user, "/quit");
		
		GameBoard board = gameModelToGameBoard(game);
		board.playerQuit(user.getId());
		writeBoardToDatabase(board);
		auditDao.addAuditLine(dto.getGameId(), new Date(), user.getId(), null, principal.getName() + " quit game.");
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	// TODO: document
	@RequestMapping(value="/join", method = RequestMethod.POST)
	@ResponseBody
	public GameStatusResponseDto joinGame(@Valid @RequestBody JoinRequestDto dto, Principal principal)
			throws BadRequestException, NotFoundException, InternalServerException, TraverseException {
		final GameModel game = gamesDao.getGameById(dto.getGameId());
		
		if(game == null) {
			throw new NotFoundException("Game not found");
		}
		
		final long userId = usersDao.getUserByUsername(principal.getName()).getId();
		
		// make sure user is not already in a game
		if (gamesDao.isPlayerCurrentlyInAGame(userId)) {
			LOG.info("Attempt to join a game while in another game. User: " + principal.getName() + " GameID (join): "
					+ dto.getGameId());
			throw new BadRequestException("User already in a game");
		}
		
		GameBoard board = gameModelToGameBoard(game);
		board.registerPlayerTwo(userId);
		writeBoardToDatabase(board);
		auditDao.addAuditLine(dto.getGameId(), new Date(), userId, null, principal.getName() + " joined game.");
		
		return new GameStatusResponseDto(dto.getGameId(), board.getBoard(), board.getGameState().getCurrentPlayerID(),
				board.getPlayerOneID(), board.getPlayerTwoID(), board.getGameState().getStatus());
	}


	// TODO: document
	@RequestMapping(value="/status", method = RequestMethod.POST)
	@ResponseBody
	public GameStatusResponseDto getStatus(@Valid @RequestBody StatusRequestDto dto, Principal principal)
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
	

	// TODO: document
	@RequestMapping(value="/move", method = RequestMethod.POST)
	@ResponseBody
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
		
		board.movePiece(dto.getPieceId(), convertCoordinatesToPoints(dto.getMoves()));
		writeBoardToDatabase(board);
		auditDao.addAuditLine(dto.getGameId(), new Date(), user.getId(), dto.getPieceId(), coordinatesToString(dto.getMoves()));
		
		return new GameStatusResponseDto(dto.getGameId(), board.getBoard(), board.getGameState().getCurrentPlayerID(),
				board.getPlayerOneID(), board.getPlayerTwoID(), board.getGameState().getStatus());
	}
	
	// TODO: way to list the games
	// TODO: way to get the move list for a given game
	// TODO: way to get win-loss record for a given user

	
	
	private GameBoard gameModelToGameBoard(GameModel model) throws InternalServerException {
		try {
			return new GameBoard(model);
		} catch (IOException e) {
			LOG.error("Error while converting GameModel to a GameBoard! GameID: " + model.getGameId(), e);
			throw new InternalServerException("Bad game state, please contact an admin");
		}
	}

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
			LOG.info("Attempt to operate on non-existent game or game in which user is not a participant. User: "
					+ user.getUsername() + " GameID: " + game.getGameId() + " Method: " + method);
			throw new NotFoundException("No game found for user");
		}
		
		return true;
	}
	
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
	
	private String coordinatesToString(List<Coordinate> coords) {
		if(coords == null) return null;
		
		// TODO: do we have a limit on the coordinates? the database can't take more than 128....
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
}

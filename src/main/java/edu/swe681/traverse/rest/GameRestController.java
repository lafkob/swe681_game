package edu.swe681.traverse.rest;

import java.io.IOException;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.Date;

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
import edu.swe681.traverse.application.exception.NotYetImplementedException;
import edu.swe681.traverse.game.GameBoard;
import edu.swe681.traverse.game.exception.TraverseException;
import edu.swe681.traverse.model.GameModel;
import edu.swe681.traverse.persistence.dao.AuditDao;
import edu.swe681.traverse.persistence.dao.GamesDao;
import edu.swe681.traverse.persistence.dao.UsersDao;
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
	 * @throws NotYetImplementedException 
	 * @throws InternalServerException 
	 * @throws TraverseException 
	 */
	@RequestMapping(value="/quit", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> quitGame(@Valid @RequestBody GameRequestDto dto, Principal principal)
			throws NotFoundException, NotYetImplementedException, InternalServerException, TraverseException {
		final GameModel game = gamesDao.getGameById(dto.getGameId());
		final long userId = usersDao.getUserByUsername(principal.getName()).getId();
		
		// make sure the user is in the game, don't give a specific error as
		// to whether the game exists
		if (game == null || !game.isUserInGame(userId)) {
			LOG.info(
					"Attempt to quit game that was either non-existent or in which the user is not a participant. User: "
							+ principal.getName() + " GameID: " + dto.getGameId());
			throw new NotFoundException("No game found for user");
		}
		
		GameBoard board = gameModelToGameBoard(game);
		board.playerQuit(userId);
		writeBoardToDatabase(board);
		auditDao.addAuditLine(dto.getGameId(), new Date(), userId, null, principal.getName() + " quit game.");
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	// TODO: document
	@RequestMapping(value="/join", method = RequestMethod.POST)
	@ResponseBody
	public GameStatusResponseDto joinGame(@Valid @RequestBody JoinRequestDto dto, Principal principal)
			throws NotYetImplementedException, BadRequestException, NotFoundException, InternalServerException, TraverseException {
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
		
		// TODO: return the full game state
		
		// TODO: remove
		throw new NotYetImplementedException();
//		return new GameStatusResponseDto(joinRequest.getGameId());
	}


	// TODO: document
	@RequestMapping(value="/status", method = RequestMethod.POST)
	@ResponseBody
	public GameStatusResponseDto getStatus(@Valid @RequestBody StatusRequestDto dto, Principal principal)
			throws NotYetImplementedException, NotFoundException {
		final GameModel game = gamesDao.getGameById(dto.getGameId());
		final long userId = usersDao.getUserByUsername(principal.getName()).getId();
		
		// make sure the user is in the game, don't give a specific error as
		// to whether the game exists
		if (game == null || !game.isUserInGame(userId)) {
			LOG.info("Attempt to get status for non-existent game or game in which user is not a participant. User: "
					+ principal.getName() + " GameID: " + dto.getGameId());
			throw new NotFoundException("No game found for user");
		}
		
		// TODO: get the game state and populate it fully

		// TODO: remove
		throw new NotYetImplementedException();
		
//		return new GameStatusResponseDto(statusRequest.getGameId());
	}
	

	// TODO: document
	@RequestMapping(value="/move", method = RequestMethod.POST)
	@ResponseBody
	public GameStatusResponseDto makeMove(@Valid @RequestBody MoveRequestDto dto, Principal principal)
			throws NotYetImplementedException, NotFoundException {
		final GameModel game = gamesDao.getGameById(dto.getGameId());
		final long userId = usersDao.getUserByUsername(principal.getName()).getId();
		
		// make sure the user is in the game, don't give a specific error as
		// to whether the game exists
		if (game == null || !game.isUserInGame(userId)) {
			LOG.info("Attempt to make a move for non-existent game or game in which user is not a participant. User: "
					+ principal.getName() + " GameID: " + dto.getGameId());
			throw new NotFoundException("No game found for user");
		}
		
		// TODO: update the game state, exceptions will be thrown and handled globally
		// TODO: audit the call

		// TODO: remove
		throw new NotYetImplementedException();
		
//		return new GameStatusResponseDto(moveRequest.getGameId());
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
}

package edu.swe681.traverse.rest;

import java.io.IOException;
import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.swe681.traverse.application.exception.BadRequestException;
import edu.swe681.traverse.application.exception.InternalServerException;
import edu.swe681.traverse.application.exception.NotFoundException;
import edu.swe681.traverse.application.exception.NotYetImplementedException;
import edu.swe681.traverse.game.GameBoard;
import edu.swe681.traverse.model.GameModel;
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
	
	@Autowired
	private GamesDao gamesDao;
	
	@Autowired
	private UsersDao usersDao;
	
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
			// TODO: audit this?
			throw new BadRequestException("User is already in a game.");
		}
		
		// TODO: auditing stuff
		final long gameId = gamesDao.startNewGame(userId);
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
	 */
	@RequestMapping(value="/quit", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> quitGame(@Valid @RequestBody GameRequestDto dto, Principal principal)
			throws NotFoundException, NotYetImplementedException, InternalServerException {
		final GameModel game = gamesDao.getGameById(dto.getGameId());
		final long userId = usersDao.getUserByUsername(principal.getName()).getId();
		
		// make sure the user is in the game, don't give a specific error as
		// to whether the game exists
		if (game == null || !game.isUserInGame(userId)) {
			// TODO: audit this?
			throw new NotFoundException("No game found for user");
		}
		
		GameBoard board = null;
		try {
			board = new GameBoard(game);
		} catch (IOException e) {
			// TODO: some kind of log
			throw new InternalServerException("Bad game state, please contact an admin");
		}
		
		// TODO: update the game state:
		// if in progress other user wins
		// if not started, remove user
		// is there an ended state?
		// TODO: auditing stuff

		// TODO: remove
		throw new NotYetImplementedException();
		
//		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	// TODO: document
	@RequestMapping(value="/join", method = RequestMethod.POST)
	@ResponseBody
	public GameStatusResponseDto joinGame(@Valid @RequestBody JoinRequestDto joinRequest) throws NotYetImplementedException {
		// TODO: get the user and make sure they are not already in a game
		// TODO: update the game state to show this user in it
		// TODO: auditing stuff
		// TODO: return the full game state
		
		// TODO: remove
		throw new NotYetImplementedException();
//		return new GameStatusResponseDto(joinRequest.getGameId());
	}


	// TODO: document
	@RequestMapping(value="/status", method = RequestMethod.POST)
	@ResponseBody
	public GameStatusResponseDto getStatus(@Valid @RequestBody StatusRequestDto statusRequest) throws NotYetImplementedException {
		// TODO: get the user and make sure they are in the game being requested
		// TODO: get the game state and populate it fully
		// TODO: audit the call?

		// TODO: remove
		throw new NotYetImplementedException();
		
//		return new GameStatusResponseDto(statusRequest.getGameId());
	}
	

	// TODO: document
	@RequestMapping(value="/move", method = RequestMethod.POST)
	@ResponseBody
	public GameStatusResponseDto makeMove(@Valid @RequestBody MoveRequestDto moveRequest) throws NotYetImplementedException {
		// TODO: get the user and make sure they are in the game for the move
		// TODO: update the game state, exceptions will be thrown and handled globally
		// TODO: audit the call

		// TODO: remove
		throw new NotYetImplementedException();
		
//		return new GameStatusResponseDto(moveRequest.getGameId());
	}
	
	// TODO: way to list the games
	// TODO: way to get the move list for a given game
	// TODO: way to get win-loss record for a given user
}

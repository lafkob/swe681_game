package edu.swe681.traverse.rest;

import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
	
	/**
	 * Starts a new game. Player will be the only player in the game.
	 * 
	 * @return Response with the new gameId
	 */
	@RequestMapping(value="/start", method = RequestMethod.POST)
	@ResponseBody
	public GameResponseDto startNewGame() {
		// TODO: get the user and make sure they are not already in an active game
		// TODO: auditing stuff
		// TODO: set up the game and get a unique id
		return new GameResponseDto(new Random().nextLong());
	}
	
	@RequestMapping(value="/quit", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> quitGame(@RequestBody GameRequestDto dto) {
		// TODO: get the user and make sure they are in the game they requested to leave
		// TODO: update the game state:
		// if in progress other user wins
		// if not started, remove user
		// is there an ended state?
		// TODO: auditing stuff
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/join", method = RequestMethod.POST)
	@ResponseBody // TODO remove this annotation, will use JSON
	public GameStatusResponseDto joinGame(@RequestBody JoinRequestDto joinRequest) {
		// TODO: get the user and make sure they are not already in a game
		// TODO: update the game state to show this user in it
		// TODO: auditing stuff
		// TODO: return the full game state
		return new GameStatusResponseDto(joinRequest.getGameId());
	}

	@RequestMapping(value="/status", method = RequestMethod.POST)
	@ResponseBody // TODO remove this annotation, will use JSON
	public String getStatus(@RequestBody StatusRequestDto statusRequest) {
		// TODO:
		return "Status for game ID: " + statusRequest.getGameId();
	}
	
	@RequestMapping(value="/move", method = RequestMethod.POST)
	@ResponseBody // TODO remove this annotation, will use JSON
	public String makeMove(@RequestBody MoveRequestDto moveRequest) {
		// TODO:
		return "Made a move for game: " + moveRequest.getGameId();
	}
	
	// TODO: want a forfeit/quit endpoint?
}

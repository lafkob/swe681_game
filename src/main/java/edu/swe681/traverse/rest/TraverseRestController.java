package edu.swe681.traverse.rest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.swe681.traverse.rest.dto.JoinRequestDto;
import edu.swe681.traverse.rest.dto.MoveRequestDto;
import edu.swe681.traverse.rest.dto.StatusRequestDto;

/**
 * REST controller for the game API.
 */
@RestController
@RequestMapping(value="/api/game")
public class TraverseRestController {
	
	@RequestMapping(value="/join", method = RequestMethod.POST)
	@ResponseBody // TODO remove this annotation, will use JSON
	public String joinGame(@RequestBody JoinRequestDto joinRequest) {
		// TODO:
		return "Joined the game. ID: " + joinRequest.getGameId();
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

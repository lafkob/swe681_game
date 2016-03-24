package edu.swe681.traverse.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/api/game")
public class TraverseRestController {
	
	@RequestMapping(value="/join", method = RequestMethod.POST)
	@ResponseBody // TODO remove this annotation, will use JSON
	public String joinGame(@RequestParam("id") String gameId) {
		// TODO:
		return "Joined the game. ID: " + gameId;
	}

	@RequestMapping(value="/status", method = RequestMethod.POST)
	@ResponseBody // TODO remove this annotation, will use JSON
	public String getStatus(@RequestParam("id") String gameId) {
		// TODO:
		return "Status for game ID: " + gameId;
	}
	
	@RequestMapping(value="/move", method = RequestMethod.POST)
	@ResponseBody // TODO remove this annotation, will use JSON
	public String makeMove(/*TODO: move param*/) {
		// TODO:
		return "Made a move!";
	}
}

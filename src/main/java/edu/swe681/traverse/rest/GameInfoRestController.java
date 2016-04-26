package edu.swe681.traverse.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.swe681.traverse.persistence.dao.GamesDao;
import edu.swe681.traverse.rest.dto.response.GameListResponseDto;
import edu.swe681.traverse.rest.dto.response.UserInfoListResponseDto;

/**
 * REST controller for the information API.
 */
@RestController
@RequestMapping(value="/api/info")
public class GameInfoRestController {
	
	@Autowired
	private GamesDao gamesDao;
	
	@RequestMapping(value="/open-games", method = RequestMethod.POST)
	@ResponseBody
	public GameListResponseDto listOpenGames() {
		return new GameListResponseDto(gamesDao.getOpenGameIds());
	}
	
	@RequestMapping(value="/finished-games", method = RequestMethod.POST)
	@ResponseBody
	public GameListResponseDto listFinishedGames() {
		return new GameListResponseDto(gamesDao.getFinishedGameIds());
	}
	
	@RequestMapping(value="/users", method = RequestMethod.POST)
	@ResponseBody
	public UserInfoListResponseDto userInfo() {
		return new UserInfoListResponseDto(gamesDao.getAllUserInfo());
	}
	
	
	

	// TODO: way to get the audit list for a given game
}

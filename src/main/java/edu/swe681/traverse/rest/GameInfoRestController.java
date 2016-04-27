package edu.swe681.traverse.rest;

import java.security.Principal;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.swe681.traverse.application.exception.BadRequestException;
import edu.swe681.traverse.game.enums.GameStatus;
import edu.swe681.traverse.model.GameModel;
import edu.swe681.traverse.model.UserModel;
import edu.swe681.traverse.persistence.dao.AuditDao;
import edu.swe681.traverse.persistence.dao.GamesDao;
import edu.swe681.traverse.persistence.dao.UsersDao;
import edu.swe681.traverse.rest.dto.request.GameRequestDto;
import edu.swe681.traverse.rest.dto.response.GameAuditListResponseDto;
import edu.swe681.traverse.rest.dto.response.GameListResponseDto;
import edu.swe681.traverse.rest.dto.response.UserInfoListResponseDto;

/**
 * REST controller for the information API.
 */
@RestController
@RequestMapping(value="/api/info")
public class GameInfoRestController {
	private static final Logger LOG = LoggerFactory.getLogger(GameInfoRestController.class);
	
	@Autowired
	private GamesDao gamesDao;
	
	@Autowired
	private AuditDao auditDao;
	
	@Autowired
	private UsersDao usersDao;
	
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
	
	@RequestMapping(value="/game", method = RequestMethod.POST)
	@ResponseBody
	public GameAuditListResponseDto gameAudit(@Valid @RequestBody GameRequestDto dto, Principal principal)
			throws BadRequestException {
		// only allow if the game is finished!
		final GameModel game = gamesDao.getGameById(dto.getGameId());
		final UserModel user = usersDao.getUserByUsername(principal.getName());
		
		// only allow the history to be returned under one of the following conditions:
		// 1. the requesting user is in the game
		// 2. the game has concluded (for non-players to see)
		if (!game.isUserInGame(user.getId()) && !isGameInFinalState(game)) {
			LOG.info("Attempt to view game audit for game in progress. User: " + principal.getName() + " GameID: "
					+ dto.getGameId());
			throw new BadRequestException("Game history not found");
		}
		
		return new GameAuditListResponseDto(dto.getGameId(), auditDao.getAuditsByGameId(dto.getGameId()));
	}

	private boolean isGameInFinalState(GameModel game) {
		return game.getGameStatus() == GameStatus.WIN || game.getGameStatus() == GameStatus.FORFEIT
				|| game.getGameStatus() == GameStatus.ENDED;
	}
}

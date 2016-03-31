package edu.swe681.traverse.rest.dto.request;


/**
 * Data for a request to make a move for a game.
 */
public class MoveRequestDto extends GameRequestDto{
	public MoveRequestDto() {
		super();
	}
	
	public MoveRequestDto(Long gameId) {
		super(gameId);
	}
}

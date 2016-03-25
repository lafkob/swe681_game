package edu.swe681.traverse.rest.dto;

/**
 * Data for a game join request.
 */
public class JoinRequestDto extends GameRequestDto {
	public JoinRequestDto() {
		super();
	}
	
	public JoinRequestDto(Long gameId) {
		super(gameId);
	}
}

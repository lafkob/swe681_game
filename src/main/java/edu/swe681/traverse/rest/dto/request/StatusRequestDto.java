package edu.swe681.traverse.rest.dto.request;

/**
 * Data for a game status request.
 */
public class StatusRequestDto extends GameRequestDto {
	public StatusRequestDto() {
		super();
	}
	
	public StatusRequestDto(Long gameId) {
		super(gameId);
	}
}

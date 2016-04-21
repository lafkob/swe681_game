package edu.swe681.traverse.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.swe681.traverse.application.exception.BadRequestException;
import edu.swe681.traverse.application.exception.NotFoundException;
import edu.swe681.traverse.application.exception.NotYetImplementedException;
import edu.swe681.traverse.rest.dto.response.MessageOnlyExceptionResponseDto;
import edu.swe681.traverse.rest.dto.response.ValidationExceptionResponseDto;

/**
 * Class in which we wire up global exception handlers for all controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandlers {

	/**
	 * Handler for bean annotation validation exceptions to return 400 - Bad
	 * Request status with detailed list of validation errors.
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ValidationExceptionResponseDto handleValidationException(MethodArgumentNotValidException e) {
		return new ValidationExceptionResponseDto(e);
	}

	/**
	 * Handler for exceptions that result in a 400 - Bad Request status with a
	 * single string error message.
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler({ BadRequestException.class })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public MessageOnlyExceptionResponseDto handleBadRequestException(BadRequestException e) {
		return new MessageOnlyExceptionResponseDto(e.getMessage());
	}

	/**
	 * Handler for exceptions that result in a 404 - Not Found status with a
	 * single string error message.
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler({ NotFoundException.class })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public MessageOnlyExceptionResponseDto handleNotFoundException(NotFoundException e) {
		return new MessageOnlyExceptionResponseDto(e.getMessage());
	}

	// TODO: this handler should go away, as we will have everything implemented :)
	/**
	 * Handler for exceptions that result in a 501 - Not Implemented status with a
	 * single string error message.
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler({ NotYetImplementedException.class })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
	public MessageOnlyExceptionResponseDto notImplementedHandler(NotYetImplementedException e) {
		return new MessageOnlyExceptionResponseDto("Method not yet implemented");
	}
}

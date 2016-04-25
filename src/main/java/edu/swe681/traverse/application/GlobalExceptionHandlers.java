package edu.swe681.traverse.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.swe681.traverse.application.exception.BadRequestException;
import edu.swe681.traverse.application.exception.InternalServerException;
import edu.swe681.traverse.application.exception.NotFoundException;
import edu.swe681.traverse.application.exception.NotYetImplementedException;
import edu.swe681.traverse.game.exception.TraverseException;
import edu.swe681.traverse.rest.dto.response.MessageOnlyExceptionResponseDto;
import edu.swe681.traverse.rest.dto.response.ValidationExceptionResponseDto;

/**
 * Class in which we wire up global exception handlers for all controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandlers {

	private static final Logger LOG = LoggerFactory.getLogger("audit");
	
	/**
	 * Handler for bean annotation validation exceptions to return 400 - Bad
	 * Request status with detailed list of validation errors.
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ValidationExceptionResponseDto handleValidationException(MethodArgumentNotValidException e) {
		//TODO: What to log here?
		return new ValidationExceptionResponseDto(e);
	}

	/**
	 * Handler for exceptions that result in a 400 - Bad Request status with a
	 * single string error message.
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler({ BadRequestException.class, TraverseException.class })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public MessageOnlyExceptionResponseDto handleBadRequestException(Exception e) {
		LOG.warn(e.getMessage());
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
	public MessageOnlyExceptionResponseDto handleNotFoundException(Exception e) {
		LOG.warn(e.getMessage());
		return new MessageOnlyExceptionResponseDto(e.getMessage());
	}
	
	/**
	 * Handler for exceptions that result in a 500 - Internal Server Error status with a
	 * single string error message.
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler({ InternalServerException.class })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageOnlyExceptionResponseDto handleInternalServerException(Exception e) {
		return new MessageOnlyExceptionResponseDto(e.getMessage());
	}
	
	/**
	 * Handler for exceptions that result in a 505 - InternalServerError status with a
	 * single string error message.
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler({ DataAccessException.class })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageOnlyExceptionResponseDto handleDataAccessException(Exception e) {
		LOG.warn(e.getMessage());
		return new MessageOnlyExceptionResponseDto("An internal error has occurred, please contact admin");
	}

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
	public MessageOnlyExceptionResponseDto notImplementedHandler(Exception e) {
		LOG.warn(e.getMessage());
		return new MessageOnlyExceptionResponseDto("Method not yet implemented");
	}
}

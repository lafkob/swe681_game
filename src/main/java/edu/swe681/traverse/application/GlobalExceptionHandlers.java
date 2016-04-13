package edu.swe681.traverse.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.swe681.traverse.rest.dto.response.ValidationExceptionResponseDto;

/**
 * Class in which we wire up global exception handlers for all controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandlers {

	@ExceptionHandler({MethodArgumentNotValidException.class})
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ValidationExceptionResponseDto handleValidationException(MethodArgumentNotValidException e) {
		return new ValidationExceptionResponseDto(e);
	}
}

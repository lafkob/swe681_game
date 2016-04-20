package edu.swe681.traverse.rest.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Response dto that bundles up field and/or object errors found on a validation exception.
 */
public class ValidationExceptionResponseDto {
	private List<ValidationError> fieldErrors;
	private List<ValidationError> globalErrors;

	public ValidationExceptionResponseDto(MethodArgumentNotValidException e) {
		if (e.getBindingResult().getFieldErrorCount() > 0) {
			fieldErrors = new ArrayList<>(e.getBindingResult().getFieldErrorCount());
			for (FieldError err : e.getBindingResult().getFieldErrors()) {
				fieldErrors.add(new ValidationError(err.getField(), err.getDefaultMessage()));
			}
		}
		
		if(e.getBindingResult().getGlobalErrorCount() > 0) {
			globalErrors = new ArrayList<>(e.getBindingResult().getGlobalErrorCount());
			for(ObjectError err : e.getBindingResult().getGlobalErrors()) {
				globalErrors.add(new ValidationError(err.getObjectName(), err.getDefaultMessage()));
			}
		}
	}
	
	public List<ValidationError> getFieldErrors() {
		return fieldErrors;
	}

	public List<ValidationError> getGlobalErrors() {
		return globalErrors;
	}


	/**
	 * Mapping from a validation error key (field or object name) to the validation message for the error.
	 */
	public static class ValidationError {
		private String key;
		private String message;
		
		public ValidationError(String key, String message) {
			Objects.requireNonNull(key, "requires a validation error key");
			Objects.requireNonNull(message, "requires a validation error message");
			this.key = key;
			this.message = message;
		}

		public String getKey() {
			return key;
		}

		public String getMessage() {
			return message;
		}
	}
}

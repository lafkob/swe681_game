package edu.swe681.traverse.rest.dto.response;

/**
 * Represents a simple DTO response that contains only an error message.
 */
public class MessageOnlyExceptionResponseDto {
	private final String errorMessage;
	
	public MessageOnlyExceptionResponseDto(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public String toString() {
		return "MessageOnlyExceptionResponseDto [errorMessage=" + errorMessage + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageOnlyExceptionResponseDto other = (MessageOnlyExceptionResponseDto) obj;
		if (errorMessage == null) {
			if (other.errorMessage != null)
				return false;
		} else if (!errorMessage.equals(other.errorMessage))
			return false;
		return true;
	}
}

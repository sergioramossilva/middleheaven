package org.middleheaven.validation;

public class ValidationException extends RuntimeException {

	
	private ValidationContext context;

	public ValidationException(ValidationContext context) {
		super();
		this.context = context;
	}

	public ValidationContext getContext() {
		return context;
	}
	
}

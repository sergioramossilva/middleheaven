package org.middleheaven.util.validation;

public class NotNullValidator<T> implements Validator<T> {

	@Override
	public ValidationResult validate(T object) {
		DefaultValidationResult result = new DefaultValidationResult();	
		
		if (object == null) {
			result.add(MessageInvalidationReason.error(object,"invalid.is.null"));
		}			
		
		return result;
	}

	
}

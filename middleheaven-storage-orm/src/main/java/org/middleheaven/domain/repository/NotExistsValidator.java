package org.middleheaven.domain.repository;


import org.middleheaven.domain.query.Query;
import org.middleheaven.util.validation.DefaultValidationResult;
import org.middleheaven.util.validation.MessageInvalidationReason;
import org.middleheaven.util.validation.ValidationResult;


public class NotExistsValidator<T> implements org.middleheaven.util.validation.Validator<T> {

	private Repository<T> repository;	

	public NotExistsValidator(Repository<T> repositorio){
		this.repository = repositorio;
	}

	@Override
	public ValidationResult validate(T object) {
		DefaultValidationResult result = new DefaultValidationResult();
		
		Query<T> query = repository.findIdentical(object);

		if (!query.isEmpty()) {
			result.add(MessageInvalidationReason.error(object,"invalid.instance.exists"));
		}			

		return result;
	}

}

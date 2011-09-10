package org.middleheaven.domain.repository;


import org.middleheaven.storage2.query.Query;
import org.middleheaven.validation.DefaultValidationResult;
import org.middleheaven.validation.MessageInvalidationReason;
import org.middleheaven.validation.ValidationResult;


public class NotExistsValidator<T> implements org.middleheaven.validation.Validator<T> {

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

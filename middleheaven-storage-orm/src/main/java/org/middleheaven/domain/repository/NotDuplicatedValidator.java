package org.middleheaven.domain.repository;

import org.middleheaven.domain.query.Query;
import org.middleheaven.util.validation.DefaultValidationResult;
import org.middleheaven.util.validation.MessageInvalidationReason;
import org.middleheaven.util.validation.ValidationResult;
import org.middleheaven.util.validation.Validator;


public class NotDuplicatedValidator<T> implements Validator<T>{

	private Repository<T> repository;	

	public NotDuplicatedValidator(Repository<T> repositorio){
		this.repository = repositorio;
	}

	@Override
	public ValidationResult validate(T object) {
		DefaultValidationResult result = new DefaultValidationResult();
		
		Query<T> query = repository.findEquals(object);

		if (!query.isEmpty()) {
			result.add(MessageInvalidationReason.error(object,"invalid.instance.duplicated"));
		}			

		return result;
	}

}

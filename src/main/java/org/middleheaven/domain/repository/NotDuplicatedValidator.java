package org.middleheaven.domain.repository;

import org.middleheaven.storage.Query;
import org.middleheaven.validation.DefaultValidationResult;
import org.middleheaven.validation.MessageInvalidationReason;
import org.middleheaven.validation.ValidationResult;
import org.middleheaven.validation.Validator;


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

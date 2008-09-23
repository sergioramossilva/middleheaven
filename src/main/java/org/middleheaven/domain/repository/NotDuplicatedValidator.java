package org.middleheaven.domain.repository;

import org.middleheaven.storage.Query;
import org.middleheaven.validation.MessageInvalidationReason;
import org.middleheaven.validation.ValidationContext;
import org.middleheaven.validation.Validator;


public class NotDuplicatedValidator<T extends Entity> implements Validator<T>{

	private EntityRepository<T> repository;	

	public NotDuplicatedValidator(EntityRepository<T> repositorio){
		this.repository = repositorio;
	}

	@Override
	public void validate(ValidationContext context, T object) {
		
		Query<T> query = repository.retriveEquals(object);

		if (!query.isEmpty()) {
			context.add(MessageInvalidationReason.error("invalid.instance.duplicated"));
		}			

	}

}

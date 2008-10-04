package org.middleheaven.domain.repository;


import org.middleheaven.storage.Query;
import org.middleheaven.validation.MessageInvalidationReason;
import org.middleheaven.validation.ValidationContext;


public class NotExistsValidator<T> implements org.middleheaven.validation.Validator<T> {

	private Repository<T> repository;	

	public NotExistsValidator(Repository<T> repositorio){
		this.repository = repositorio;
	}

	@Override
	public void validate(ValidationContext context, T object) {

		Query<T> query = repository.retriveSame(object);

		if (!query.isEmpty()) {
			context.add(MessageInvalidationReason.error("invalid.instance.exists"));
		}			

	}

}

package org.middleheaven.validation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class CompositeValidator<T> implements Validator<T> {

	Set<Validator<T>> valdiators = new HashSet<Validator<T>>();
	
	public static <E> CompositeValidator<E> compose(Validator<E> a , Validator<E> b){
		return new CompositeValidator<E>().add(a).add(b);
	}
	
	public CompositeValidator(){}
	
	public CompositeValidator<T> add(Validator<T> validator){
		valdiators.add(validator);
		return this;
	}
	
	public CompositeValidator<T> remove(Validator<T> validator){
		valdiators.add(validator);
		return this;
	}
	
	@Override
	public void validate(ValidationContext context, T object) {
		for (Iterator<Validator<T>> it = valdiators.iterator(); context.isValid() && it.hasNext();  ){
			it.next().validate(context, object);
		}
	}

}

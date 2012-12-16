package org.middleheaven.util.validation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.util.collections.Enumerable;

/**
 * Validates a bean object using a {@code Validator} for each property of interest
 *
 * @param <T>
 * 
 * @see Validator
 */
public class BeanValidator<T> extends CompositeValidator<T> {

	
	@SuppressWarnings("unchecked")
	private Map<String , CompositeValidator> propertyValidators = new HashMap<String , CompositeValidator>(); 
	
	
	public <E> BeanValidator<T> addPropertyValidator(String propertyName, Validator<E> validator, Validator<E> ... validators){
		@SuppressWarnings("unchecked") CompositeValidator<E> comp = propertyValidators.get(propertyName.toLowerCase());
		
		if (comp==null){
			
			comp = new CompositeValidator<E>();
			propertyValidators.put(propertyName.toLowerCase(), comp);
		}
		
		comp.add(validator);
		
		for(Validator<E> v: validators){
			comp.add(v);
		}

		return this;
	}
	
	public BeanValidator<T> add(Validator<? extends T> validator){
		 super.add(validator);
		 return this;
	}
	
	public BeanValidator<T> remove(Validator<? extends T> validator){
		super.add(validator);
		return this;
	}
	
	
	@Override
	public final ValidationResult validate(T object) {
		DefaultValidationResult result = new DefaultValidationResult();
		
		Enumerable<PropertyAccessor> properties = Introspector.of(object).introspectClass().inspect().properties().retriveAll();
		
		for (Iterator<PropertyAccessor> it =  properties.iterator(); result.isStrictlyValid() && it.hasNext();){
			final PropertyAccessor pa = it.next();
			
			@SuppressWarnings("unchecked") Validator<Object> validator = propertyValidators.get(pa.getName().toLowerCase());
			
			if(validator != null){
				ValidationResult propertyResult = validator.validate(pa.getValue(object));
				
				if (!propertyResult.isStrictlyValid()){
					for (InvalidationReason r : propertyResult){
						result.add(new PropertyInvalidationReason(pa.getName(), r));
					}
				}
			}
		}
		
		if (result.isStrictlyValid()){
			return super.validate(object);
		}
		
		return result;
	}

}

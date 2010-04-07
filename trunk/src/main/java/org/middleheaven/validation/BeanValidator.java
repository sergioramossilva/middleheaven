package org.middleheaven.validation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.PropertyAccessor;

/**
 * Validates a bean object using a {@code Validator} for each property of interest
 *
 * @param <T>
 * 
 * @see Validator
 */
public class BeanValidator<T> extends CompositeValidator<T> {

	
	private Map<String , CompositeValidator> propertyValidators = new HashMap<String , CompositeValidator>(); 
	
	public <E> BeanValidator<T> addPropertyValidator(String propertyName, Validator<E> validator, Validator<E> ... validators){
		CompositeValidator<E> comp = propertyValidators.get(propertyName.toLowerCase());
		
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
	
	@Override
	public final ValidationResult validate(T object) {
		DefaultValidationResult result = new DefaultValidationResult();
		
		Collection<PropertyAccessor> properties = Introspector.of(object).introspectClass().inspect().properties().retriveAll();
		
		for (Iterator<PropertyAccessor> it =  properties.iterator(); result.isStrictlyValid() && it.hasNext();){
			final PropertyAccessor pa = it.next();
			Validator validator = propertyValidators.get(pa.getName().toLowerCase());
			
			ValidationResult propertyResult = validator.validate(pa.getValue(object));
			
			if (!propertyResult.isStrictlyValid()){
				result.add(new PropertyInvalidationReason(pa.getName(), propertyResult));
			}
		}
		
		if (result.isStrictlyValid()){
			return super.validate(object);
		}
		
		return result;
	}

}

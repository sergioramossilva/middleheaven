package org.middleheaven.validation;

/**
 * Encapsulates the logic to validate an object 
 *
 * @param <T> the type of object this validator can validate.
 */
public interface Validator<T> {

	
	public void validate ( ValidationContext context , T object ); 
}

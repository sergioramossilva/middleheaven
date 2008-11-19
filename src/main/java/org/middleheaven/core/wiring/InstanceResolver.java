package org.middleheaven.core.wiring;

/**
 * Holds the real instance to be retrieved.
 * This useful when you want to bind a contact to an already existing object.
 * 
 * @param <T> the type for the instance to retrieve
 */
public class InstanceResolver<T> implements Resolver<T> {

	T object;
	InstanceResolver(T object){
		this.object = object;
	}
	
	@Override
	public T resolve(WiringSpecification<T> query) {
		return object;
	}


}

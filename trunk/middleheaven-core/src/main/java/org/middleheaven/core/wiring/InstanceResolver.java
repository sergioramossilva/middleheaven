package org.middleheaven.core.wiring;

/**
 * Holds the real instance to be retrieved.
 * This useful when you want to bind a contact to an already existing object.
 * 
 * @param <T> the type for the instance to retrieve
 */
public class InstanceResolver implements Resolver {

	Object object;
	
	public InstanceResolver(Object object){
		this.object = object;
	}
	
	@Override
	public Object resolve(ResolutionContext context, WiringQuery query) {
		return object;
	}


}

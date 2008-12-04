package org.middleheaven.core.wiring;

import java.util.Map;


public interface WiringContext {

	/**
	 * Finds and returns the correct instance that implements the type passed as parameter
	 * 
	 * @param <T>
	 * @param type the base type that must be matched
	 * @return An instance compatible with the passed type.
	 */
	public <T> T getInstance(Class<T> type);

	public <T> T getInstance(Class<T> type, Map<String,String> params);
	
	/**
	 * Inspects the passed object and wire the correct dependencies defined for this object class
	 * @param object whose dependencies are to be provided
	 */
	public void wireMembers(Object object);
	
	/**
	 * Adds one or more <code>BindingConfiguration</code>s to this context. 
	 * @param configuration one or more <code>BindingConfiguration</code> to be added to the context
	 * @return this WiringContext correctly configurated.
	 */
	public WiringContext addConfiguration(BindConfiguration ... configuration); 

}

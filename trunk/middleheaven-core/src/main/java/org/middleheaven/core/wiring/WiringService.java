package org.middleheaven.core.wiring;

import java.util.Map;


/**
 * The {@link WiringService} is responsible for the gathering, instantiation and injection of all dependencies 
 * within its context. Objects do not have to be created by this services.
 * 
 * 
 * 
 * 
 */
public interface WiringService  {

	/**
	 * Finds and returns the correct instance that implements the type passed as parameter
	 * 
	 * @param <T>
	 * @param type the base type that must be matched
	 * @return An instance compatible with the passed type.
	 */
	public <T> T getInstance(Class<T> type);

	public <T> T getInstance(Class<T> type, Map<String, ? extends Object> params);
	
	/**
	 * Inspects the passed object and wire the correct dependencies defined for this object class
	 * 
	 * @param object whose dependencies are to be provided
	 */
	public void wireMembers(Object object);
	
	/**
	 * Adds one or more <code>BindingConfiguration</code>s to this context. 
	 * @param configuration one or more <code>BindingConfiguration</code> to be added to the context
	 * @return this WiringContext correctly configurated.
	 */
	public WiringService addConfiguration(BindConfiguration ... configuration);

	/**
	 * Register a user defined scope.
	 *  
	 * @param name the name of the scope.
	 * @param scope the scope to be added
	 */
	public void registerScope(String name, Scope scope);
	
	/**
	 * Add an {@link ObjectPoolListener} that will be informed when an object is added or removed from the pool.
	 * 
	 * @param listener the listener to be informed.
	 */
	public void addObjectCycleListener(ObjectPoolListener listener);
	
	/**
	 * Remove an {@link ObjectPoolListener} 
	 * 
	 * @param listener the listener to be removed.
	 */
	public void removeObjectCycleListener(ObjectPoolListener listener);
	

	/**
	 * Returns the {@link ProfilesBag} for active profiles. 
	 * 
	 * @return the {@link ProfilesBag} for active profiles. 
	 */
	public ProfilesBag getActiveProfiles();

	
	/**
	 * Adds one or more {@link WiringConnector}s to the service. 
	 * 
	 * This allows for further configuration of the binder.
	 * 
	 * @see ConnectableBinder
	 * @param connectors the {@link WiringConnector} to add.
	 */
	public void addConnector(WiringConnector connector); 
	

	/**
	 * Close the wiring service.
	 */
	public void close();
	
	
	public WiringService addItemBundle(WiringItemBundle bundle);
	
	public WiringService addItem(WiringItem item);
	
	
	/**
	 * Initiate scan
	 */
	public void refresh();
}

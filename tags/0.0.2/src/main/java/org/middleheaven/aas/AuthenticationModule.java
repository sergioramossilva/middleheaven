package org.middleheaven.aas;

import java.util.Set;


/**
 * Responsible for acquiring and authenticating access credentials. 
 */
public interface AuthenticationModule {

	/**
	 * Adds the needed {@code Callback}s to the current {@code CallbacksSet}; 
	 * @param callbackSet
	 */
	public void registerCallbacks(CallbacksSet callbackSet);
	
	public void autenticate(Set<Credential> credentials, CallbacksSet callbackSet) throws AuthenticationException;



}

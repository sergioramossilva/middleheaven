/**
 * 
 */
package org.middleheaven.process.web.server.filters;

import java.io.Serializable;

import org.middleheaven.aas.AccessException;
import org.middleheaven.aas.CallbackHandler;
import org.middleheaven.aas.CallbacksSet;

/**
 * 
 */
public class AccessControlCallbackHandler implements CallbackHandler , Serializable{


	private static final long serialVersionUID = 6897036338677468538L;
	
	final CallbacksSet set = new CallbacksSet();
	
	public AccessControlCallbackHandler (){
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CallbacksSet getCallbacks() {
		return set;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onException(AccessException e) {
		//no-op
	}

}

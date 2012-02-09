/**
 * 
 */
package org.middleheaven.persistance;

import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;

/**
 * 
 */
public class DataPersistanceServiceActivator extends Activator {

	DefaultDataService service;
	
	@Publish
	public DataService getDataPersistanceService(){
		return service;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ActivationContext context) {
		service = new DefaultDataService();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ActivationContext context) {
		service.close();
		service = null;
	}

}

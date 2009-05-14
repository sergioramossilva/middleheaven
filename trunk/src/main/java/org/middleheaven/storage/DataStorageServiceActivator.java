package org.middleheaven.storage;

import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;

public class DataStorageServiceActivator extends Activator {

	 DataStorageService service = new HashDataStorageService();
	 
	@Publish
	public DataStorageService getService() {
		return service;
	}

	@Override
	public void activate(ActivationContext context) {
		
	}

	@Override
	public void inactivate(ActivationContext context) {
		
	} 

}

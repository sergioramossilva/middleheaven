package org.middleheaven.storage;

import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.core.wiring.activation.Publish;

public class DataStorageServiceActivator extends ServiceActivator {

	 DataStorageService service = new HashDataStorageService();
	 
	@Publish
	public DataStorageService getService() {
		return service;
	}

	@Override
	public void activate(ServiceAtivatorContext context) {

	}

	@Override
	public void inactivate(ServiceAtivatorContext context) {
		
	} 

}

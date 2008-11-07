package org.middleheaven.storage;

import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.discover.ServiceActivator;

public class DataStorageServiceActivator extends ServiceActivator {

	 DataStorageService service = new HashDataStorageService();
	 
	@Override
	public void activate(ServiceContext context) {
		context.register(DataStorageService.class, service, null);
	}

	@Override
	public void inactivate(ServiceContext context) {
		context.unRegister(DataStorageService.class, service, null);
	} 

}

package org.middleheaven.aas;

import org.middleheaven.core.services.Publish;
import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;

public class AccessControlActivator extends ServiceActivator {

	
	AccessControlService controler;
	
	@Publish
	public AccessControlService getAccessControlService(){
		return this.controler;
	}
	
	@Override
	public void activate(ServiceAtivatorContext context) {
		controler = new AccessController();
	}

	@Override
	public void inactivate(ServiceAtivatorContext context) {
		controler = null;
	}

}

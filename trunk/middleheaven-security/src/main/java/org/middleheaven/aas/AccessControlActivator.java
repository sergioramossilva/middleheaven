package org.middleheaven.aas;

import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;

public class AccessControlActivator extends Activator {

	
	AccessControlService controler;
	
	@Publish
	public AccessControlService getAccessControlService(){
		return this.controler;
	}

	@Override
	public void activate() {
		controler = new StandardAccessControlService();
	}

	@Override
	public void inactivate() {
		controler = null;
	}

}

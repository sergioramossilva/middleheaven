package org.middleheaven.aas;

import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;

public class AccessControlActivator extends Activator {

	
	AccessControlService controler;
	
	@Publish
	public AccessControlService getAccessControlService(){
		return this.controler;
	}

	@Override
	public void activate(ActivationContext context) {
		controler = new WebAccessController();
	}

	@Override
	public void inactivate(ActivationContext context) {
		controler = null;
	}

}

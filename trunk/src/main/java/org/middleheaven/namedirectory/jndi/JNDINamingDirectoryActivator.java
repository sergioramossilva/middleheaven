package org.middleheaven.namedirectory.jndi;

import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.namedirectory.NameDirectoryService;

public class JNDINamingDirectoryActivator extends Activator {

	private JNDINameDirectoryService service;
	
	@Publish
	public NameDirectoryService getNameDirectoryService(){
		return service;
	}
	
	@Override
	public void activate(ActivationContext context) {
		service =  new JNDINameDirectoryService();
	}

	@Override
	public void inactivate(ActivationContext context) {
		service = null;
	}

}

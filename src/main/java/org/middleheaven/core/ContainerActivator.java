package org.middleheaven.core;

import org.middleheaven.core.services.ContainerService;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.discover.ServiceActivator;

public class ContainerActivator extends ServiceActivator {
    

	public SimpleContainerService service;
	
    public ContainerActivator(Container container) {
		this.service= new SimpleContainerService(container);
	}

	@Override
	public void activate(ServiceContext context) {
		context.register(ContainerService.class, service, null);
	}



	@Override
	public void inactivate(ServiceContext context) {
		// no-op
	}
    
    private class SimpleContainerService implements ContainerService{
    	public Container container;
		public SimpleContainerService(Container container) {
			this.container = container;
		}

		@Override
		public Container getContainer() {
			return container;
		}
    	
    }






}

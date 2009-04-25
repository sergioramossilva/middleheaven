package org.middleheaven.test.core.service;

import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Wire;

public class ActivatorB extends ServiceActivator {

	A service;
	
	@Wire 
	public void setService(A service){
		this.service = service;
	}
	
	@Publish 
	public B getService(){
		return new B(){

			@Override
			public A getA() {
				return service;
			}
			
		};
	}
	
	@Override
	public void activate(ServiceAtivatorContext context) {

		if (service == null){
			 throw new IllegalArgumentException();
		}
	}

	@Override
	public void inactivate(ServiceAtivatorContext context) {
		// TODO Auto-generated method stub

	}

}

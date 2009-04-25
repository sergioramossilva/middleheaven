package org.middleheaven.test.core.service;

import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Wire;

public class ActivatorA extends ServiceActivator {

	B service;
	
	@Wire 
	public void setService(B service){
		this.service = service;
	}
	
	@Publish 
	public A getService(){
		return new A(){

			@Override
			public B getB() {
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

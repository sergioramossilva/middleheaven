package org.middleheaven.test.core.service;

import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Wire;

public class ActivatorA extends Activator {

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
	public void activate() {
		if (service == null){
			 throw new IllegalArgumentException();
		}
	}

	@Override
	public void inactivate() {
		// TODO implement Activator.inactivate
		
	}

}

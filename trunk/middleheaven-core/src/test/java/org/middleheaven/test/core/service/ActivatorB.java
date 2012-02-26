package org.middleheaven.test.core.service;

import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Wire;

public class ActivatorB extends Activator {

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

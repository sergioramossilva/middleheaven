package org.middleheaven.core.service;

import org.middleheaven.core.services.Publish;
import org.middleheaven.core.services.Require;
import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;

public class ActivatorB extends ServiceActivator {

	A service;
	
	@Require 
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

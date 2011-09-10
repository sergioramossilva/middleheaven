/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.core.bootstrap;

import org.middleheaven.application.ApplicationLoadingCycle;
import org.middleheaven.application.ApplicationLoadingService;
import org.middleheaven.application.DynamicLoadApplicationServiceActivator;
import org.middleheaven.core.services.ServiceNotAvailableException;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.logging.Log;
import org.middleheaven.ui.service.UIServiceActivator;

/**
 * 
 */
public class StandaloneBootstrap extends ExecutionEnvironmentBootstrap {

	BootstrapContainer env;
	private Object starter;

	public StandaloneBootstrap(Object starter , BootstrapContainer env){
		this.env = env;
		this.starter = starter;
	}

	@Override
	public void posConfig(BootstrapContext context){

		context.addActivator(DynamicLoadApplicationServiceActivator.class)
		.addActivator(UIServiceActivator.class)
		;

	}

	ApplicationLoadingCycle appCycle;

	protected void doAfterStart(){
		
		try{
			ApplicationLoadingService app = ServiceRegistry.getService(ApplicationLoadingService.class);
			if (app!=null){
				appCycle = app.getApplicationLoadingCycle();
				appCycle.start();
			}
		} catch (ServiceNotAvailableException e){
			Log.onBookFor(this.getClass()).warn("Executing without Application Cycle Service");
		}
	}

	protected void doBeforeStop(){
		if (appCycle!=null){
			appCycle.stop();
		}
	}
	
	protected void doAfterStop(){
		System.exit(0);
	}

	@Override
	public BootstrapContainer getContainer() {
		return env;
	}



}

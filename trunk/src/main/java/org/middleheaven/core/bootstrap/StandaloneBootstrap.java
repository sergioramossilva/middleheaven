/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.core.bootstrap;

import org.middleheaven.application.ApplicationLoadingCycle;
import org.middleheaven.application.ApplicationLoadingCycleService;
import org.middleheaven.core.Container;
import org.middleheaven.core.ContextIdentifier;
import org.middleheaven.core.services.ServiceNotFoundException;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.logging.Logging;


/**
 * @author  Sergio M. M. Taborda 
 */
public class StandaloneBootstrap extends ExecutionEnvironmentBootstrap {

	Container env;

	public StandaloneBootstrap(Container env){
		this.env = env;
	}

	@Override
	public ContextIdentifier getContextIdentifier() {
		return ContextIdentifier.getInstance("app");
	}

	ApplicationLoadingCycle appCycle;

	protected void doAfterStart(){
		try{
			ApplicationLoadingCycleService app = ServiceRegistry.getService(ApplicationLoadingCycleService.class);
			if (app!=null){
				appCycle = app.getApplicationLoadingCycle();
				appCycle.start();
			}
		} catch (ServiceNotFoundException e){
			Logging.getBook(this.getClass()).warn("Executing without Application Cycle Service");
		}
	}

	protected void doAfterStop(){
		if (appCycle!=null){
			appCycle.stop();
		}
		System.exit(0);
	}

	@Override
	public Container getContainer() {
		return env;
	}



}

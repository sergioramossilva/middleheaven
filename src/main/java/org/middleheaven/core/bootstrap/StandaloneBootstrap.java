/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.core.bootstrap;

import org.middleheaven.application.ApplicationLoadingCycle;
import org.middleheaven.application.ApplicationLoadingService;
import org.middleheaven.application.DynamicLoadApplicationServiceActivator;
import org.middleheaven.core.Container;
import org.middleheaven.core.ContextIdentifier;
import org.middleheaven.core.services.ServiceContextConfigurator;
import org.middleheaven.core.services.ServiceNotFoundException;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.services.engine.ActivatorBagServiceDiscoveryEngine;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.logging.Logging;
import org.middleheaven.ui.UIServiceActivator;


/**
 * @author  Sergio M. M. Taborda 
 */
public class StandaloneBootstrap extends ExecutionEnvironmentBootstrap {

	Container env;
	private Object starter;

	public StandaloneBootstrap(Object starter , Container env){
		this.env = env;
		this.starter = starter;
	}

	@Override
	public ContextIdentifier getContextIdentifier() {
		return ContextIdentifier.getInstance("app");
	}

	public void configuate(ServiceContextConfigurator configurator){
		ActivatorBagServiceDiscoveryEngine engine = new ActivatorBagServiceDiscoveryEngine()
		.addActivator(DynamicLoadApplicationServiceActivator.class)
		.addActivator(UIServiceActivator.class)
		;
		
		configurator.addEngine(engine);
	}

	ApplicationLoadingCycle appCycle;

	protected void doAfterStart(){
		
		 
        ServiceRegistry.getService(WiringService.class).getWiringContext().wireMembers(starter);
        
		try{
			ApplicationLoadingService app = ServiceRegistry.getService(ApplicationLoadingService.class);
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

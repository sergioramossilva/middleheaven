/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.core.bootstrap.client;

import org.middleheaven.application.DynamicLoadApplicationServiceActivator;
import org.middleheaven.core.bootstrap.AbstractBootstrap;
import org.middleheaven.core.bootstrap.BootstrapContext;
import org.middleheaven.core.bootstrap.BootstrapEnvironment;
import org.middleheaven.core.bootstrap.BootstrapEnvironmentResolver;
import org.middleheaven.logging.LogServiceDelegatorLogger;
import org.middleheaven.logging.LoggingService;
import org.middleheaven.ui.desktop.service.DesktopUIServiceActivator;

/**
 * 
 */
public class DesktopBootstrap extends AbstractBootstrap {

	
	public static void main(String[] args){
		DesktopBootstrap bootstrap = new DesktopBootstrap();
		bootstrap.start();
	}
	
	public DesktopBootstrap(){
	
	}


	@Override
	public void posConfig(BootstrapContext context){

		context.addActivator(DynamicLoadApplicationServiceActivator.class)
		.addActivator(DesktopUIServiceActivator.class)
		;

	}

	protected void doAfterStop(BootstrapContext context){
		System.exit(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getExecutionConfiguration() {
		return "desktop";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BootstrapEnvironmentResolver bootstrapEnvironmentResolver() {
		return new BootstrapEnvironmentResolver(){

			@Override
			public BootstrapEnvironment resolveEnvironment(
					BootstrapContext context) {
				
				LoggingService loggingService = context.getServiceContext().getService(LoggingService.class);
				
				return new DesktopUIBootstrapEnvironment(new LogServiceDelegatorLogger(this.getClass().getName(), loggingService));
			}
			
		};
	}



}

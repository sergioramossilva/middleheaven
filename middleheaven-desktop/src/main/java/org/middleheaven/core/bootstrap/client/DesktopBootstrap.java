/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.core.bootstrap.client;

import org.middleheaven.application.DynamicLoadApplicationServiceActivator;
import org.middleheaven.core.bootstrap.BootstrapContainer;
import org.middleheaven.core.bootstrap.ExecutionContext;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.logging.LogServiceDelegatorLogger;
import org.middleheaven.ui.desktop.service.DesktopUIServiceActivator;

/**
 * 
 */
public class DesktopBootstrap extends ExecutionEnvironmentBootstrap {

	
	public static void main(String[] args){
		DesktopBootstrap bootstrap = new DesktopBootstrap();
		bootstrap.start();
	}
	
	public DesktopBootstrap(){}


	@Override
	public void posConfig(ExecutionContext context){

		context.addActivator(DynamicLoadApplicationServiceActivator.class)
		.addActivator(DesktopUIServiceActivator.class)
		;

	}

	protected void doAfterStop(){
		System.exit(0);
	}

	@Override
	public BootstrapContainer resolveContainer() {
		return new DesktopUIContainer(new LogServiceDelegatorLogger(this.getClass().getName(), this.resolveLoggingService()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getExecutionConfiguration() {
		return "desktop";
	}



}

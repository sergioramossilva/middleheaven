package org.middleheaven.logging;

import java.net.MalformedURLException;

import org.middleheaven.core.bootstrap.ContainerFileSystem;
import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.logging.config.LoggingConfiguration;
import org.middleheaven.logging.config.LoggingConfigurator;
import org.middleheaven.logging.config.XMLLoggingConfigurator;

public class LoggingActivator extends Activator {

	ContainerFileSystem fileRepositoryService;
	LoggingService loggingService;
	
	public LoggingActivator(){}

	@Wire
	public void setFileRepositoryService(ContainerFileSystem fileRepositoryService) {
		this.fileRepositoryService = fileRepositoryService;
	}

	@Publish
	public LoggingService getLoggingService() {
		return loggingService;
	}

	@Override
	public void activate(ActivationContext context) {

		ManagedFile configFolder = fileRepositoryService.getEnvironmentConfigRepository();

		LoggingConfigurator configurator = new BasicConfigurator();
		LoggingConfiguration configuration = new LoggingConfiguration(null);

		if (configFolder!=null && configFolder.exists() && configFolder.isReadable()){
			ManagedFile configXML = configFolder.retrive("log-config.xml");
			if (configXML!=null && configXML.exists() && configXML.isReadable()){
				try {
					configurator = new XMLLoggingConfigurator(configXML.getURI().toURL());
				} catch (MalformedURLException e) {
					throw new IllegalArgumentException(e);
				}
				ManagedFile logFolder = fileRepositoryService.getAppLogRepository();
				configuration = new LoggingConfiguration(logFolder);
			}
		} 
		//else,  don't bother to configure
		configurator = new BasicConfigurator();
		configuration = new LoggingConfiguration(null);


		// there are no special properties for this service
		this.loggingService = new HashLoggingService(configuration, configurator);

	}

	@Override
	public void inactivate(ActivationContext context) {
		loggingService = null;
	}


}

package org.middleheaven.logging;

import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.service.CommonRepositories;
import org.middleheaven.io.repository.service.FileRepositoryRegistryService;
import org.middleheaven.logging.config.LoggingConfiguration;
import org.middleheaven.logging.config.LoggingConfigurator;
import org.middleheaven.logging.config.XMLLoggingConfigurator;

public class LoggingActivator extends Activator {

	FileRepositoryRegistryService fileRepositoryService;
	LoggingService loggingService;
	
	public LoggingActivator(){}

	@Wire
	public void setFileRepositoryService(FileRepositoryRegistryService fileRepositoryService) {
		this.fileRepositoryService = fileRepositoryService;
	}

	@Publish
	public LoggingService getLoggingService() {
		return loggingService;
	}

	@Override
	public void activate(ActivationContext context) {

		ManagedFile configFolder = fileRepositoryService.getRepository(CommonRepositories.ENV_CONFIGURATION);

		LoggingConfigurator configurator = new BasicConfigurator();
		LoggingConfiguration configuration = new LoggingConfiguration(null);

		if (configFolder!=null && configFolder.exists() && configFolder.isReadable()){
			ManagedFile configXML = configFolder.retrive("log-config.xml");
			if (configXML!=null && configXML.exists() && configXML.isReadable()){
				configurator = new XMLLoggingConfigurator(configXML.getURL());
				ManagedFile logFolder = fileRepositoryService.getRepository(CommonRepositories.APP_LOG);
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

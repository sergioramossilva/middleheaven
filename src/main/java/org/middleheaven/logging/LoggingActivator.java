package org.middleheaven.logging;

import org.middleheaven.core.services.Publish;
import org.middleheaven.core.services.Require;
import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.service.CommonRepositories;
import org.middleheaven.io.repository.service.FileRepositoryService;
import org.middleheaven.logging.config.LoggingConfiguration;
import org.middleheaven.logging.config.LoggingConfigurator;
import org.middleheaven.logging.config.XMLLoggingConfigurator;

public class LoggingActivator extends ServiceActivator {

	FileRepositoryService fileRepositoryService;
	LoggingService loggingService;
	
	public LoggingActivator(){}

	@Require
	public void setFileRepositoryService(FileRepositoryService fileRepositoryService) {
		this.fileRepositoryService = fileRepositoryService;
	}

	@Publish
	public LoggingService getLoggingService() {
		return loggingService;
	}

	@Override
	public void activate(ServiceAtivatorContext context) {

		ManagedFile configFolder = fileRepositoryService.getRepository(CommonRepositories.ENV_CONFIGURATION);

		LoggingConfigurator configurator = new BasicConfigurator();
		LoggingConfiguration configuration = new LoggingConfiguration(null);

		if (configFolder!=null && configFolder.exists() && configFolder.isReadable()){
			ManagedFile configXML = configFolder.resolveFile("log-config.xml");
			if (configXML!=null && configXML.exists() && configXML.isReadable()){
				configurator = new XMLLoggingConfigurator(configXML.getURL());
				ManagedFile logFolder = fileRepositoryService.getRepository(CommonRepositories.LOG);
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
	public void inactivate(ServiceAtivatorContext context) {
		// no-op
	}


}

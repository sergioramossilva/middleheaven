package org.middleheaven.logging;

import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.services.engine.ServiceActivator;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.service.CommonRepositories;
import org.middleheaven.io.repository.service.FileRepositoryService;
import org.middleheaven.logging.config.LoggingConfiguration;
import org.middleheaven.logging.config.LoggingConfigurator;
import org.middleheaven.logging.config.XMLLoggingConfigurator;

public class LoggingActivator extends ServiceActivator {

	@Override
	public void activate(ServiceContext context) {

		FileRepositoryService service = ServiceRegistry.getService(FileRepositoryService.class);

		ManagedFile configFolder = service.getRepository(CommonRepositories.ENV_CONFIGURATION);

		LoggingConfigurator configurator = new BasicConfigurator();
		LoggingConfiguration configuration = new LoggingConfiguration(null);

		if (configFolder!=null && configFolder.exists() && configFolder.isReadable()){
			ManagedFile configXML = configFolder.resolveFile("log-config.xml");
			if (configXML!=null && configXML.exists() && configXML.isReadable()){
				configurator = new XMLLoggingConfigurator(configXML.getURL());
				ManagedFile logFolder = service.getRepository(CommonRepositories.LOG);
				configuration = new LoggingConfiguration(logFolder);
			}
		} 
		//else,  don't bother to configure
		configurator = new BasicConfigurator();
		configuration = new LoggingConfiguration(null);


		// there are no special properties for this service
		ServiceRegistry.register(LoggingService.class, new HashLoggingService(configuration, configurator), null);

	}

	@Override
	public void inactivate(ServiceContext context) {
		// no-op
	}


}

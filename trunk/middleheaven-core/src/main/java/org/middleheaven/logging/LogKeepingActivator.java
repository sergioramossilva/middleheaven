package org.middleheaven.logging;

import java.net.MalformedURLException;
import java.util.Collection;

import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.bootstrap.ContainerFileSystem;
import org.middleheaven.core.bootstrap.activation.ServiceActivator;
import org.middleheaven.core.bootstrap.activation.ServiceSpecification;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.logging.config.BasicConfigurator;
import org.middleheaven.logging.config.LoggingConfiguration;
import org.middleheaven.logging.config.LoggingConfigurator;
import org.middleheaven.logging.config.XMLLoggingConfigurator;

/**
 * Ativates log keeping. This activator will create a {@link ConfigurableLogListener}
 * and will register it with the environment {@link LoggingService}.
 */
public class LogKeepingActivator extends ServiceActivator {

	LoggingService loggingService;
	
	public LogKeepingActivator(){}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(new ServiceSpecification(LoggingService.class));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		
	}
	

	@Override
	public void activate(ServiceContext serviceContext) {

		final ContainerFileSystem fileRepositoryService = serviceContext.getService(BootstrapService.class).getEnvironmentBootstrap().getContainer().getFileSystem();
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

		ConfigurableLogListener listener = new ConfigurableLogListener(configuration, configurator);

		this.loggingService.addLogListener(listener);
	}

	@Override
	public void inactivate(ServiceContext serviceContext) {
		loggingService = null;
	}



}

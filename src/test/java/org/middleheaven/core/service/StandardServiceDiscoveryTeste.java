package org.middleheaven.core.service;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;
import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.StandaloneBootstrap;
import org.middleheaven.core.bootstrap.client.DesktopUIContainer;
import org.middleheaven.core.services.ServiceContextConfigurator;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.services.engine.LocalFileRepositoryDiscoveryEngine;
import org.middleheaven.io.repository.ManagedFileRepositories;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.logging.LoggingService;


public class StandardServiceDiscoveryTeste {

	@BeforeClass
	public static void setUp(){
		Container container = new DesktopUIContainer(ManagedFileRepositories.resolveFile(new File(".")));
		StandaloneBootstrap bootstrap = new StandaloneBootstrap(container);
		bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));
		
	}
	
	@Test
	public void testLoad(){
		new ServiceContextConfigurator().addEngine(new LocalFileRepositoryDiscoveryEngine());
		// LoggingService depends upon ManagedFileService 
		ServiceRegistry.getService(LoggingService.class);
	}
}

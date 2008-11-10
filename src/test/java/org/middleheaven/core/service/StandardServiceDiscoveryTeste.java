package org.middleheaven.core.service;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.StandaloneBootstrap;
import org.middleheaven.core.bootstrap.client.DesktopUIContainer;
import org.middleheaven.core.services.ServiceContextConfigurator;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.services.engine.ActivatorBagServiceDiscoveryEngine;
import org.middleheaven.core.services.engine.LocalFileRepositoryDiscoveryEngine;
import org.middleheaven.io.repository.ManagedFileRepositories;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.logging.LoggingService;


public class StandardServiceDiscoveryTeste {

	@Before
	public void setUp(){
		Container container = new DesktopUIContainer(ManagedFileRepositories.resolveFile(new File(".")));
		StandaloneBootstrap bootstrap = new StandaloneBootstrap(container);
		bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));
		
	}
	
	@Test
	public void testDiscovery(){
		new ServiceContextConfigurator().addEngine(new LocalFileRepositoryDiscoveryEngine());
		// LoggingService depends upon ManagedFileService 
		ServiceRegistry.getService(LoggingService.class);
	}
	
	@Test
	public void testCiclicDiscovery(){
		
		ActivatorBagServiceDiscoveryEngine engine = new ActivatorBagServiceDiscoveryEngine();
		
		// A depends on B , and B on A
		engine.addActivator(ActivatorA.class);
		engine.addActivator(ActivatorB.class);
		
		new ServiceContextConfigurator().addEngine(engine);
		
		B b = ServiceRegistry.getService(B.class);
		A a = ServiceRegistry.getService(A.class);
		
		assertEquals(a,b.getA());
		assertEquals(b,a.getB());
	}
	
	
}

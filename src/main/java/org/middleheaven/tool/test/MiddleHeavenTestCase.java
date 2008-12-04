package org.middleheaven.tool.test;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.middleheaven.core.services.ServiceContextConfigurator;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.services.engine.ActivatorBagServiceDiscoveryEngine;
import org.middleheaven.core.wiring.WiringContext;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;

public abstract class MiddleHeavenTestCase {


	private TestBootstrap bootstrap = new TestBootstrap(new File("."));
	
	@Before
	public void setUp(){
		
		bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));
		ActivatorBagServiceDiscoveryEngine engine = new ActivatorBagServiceDiscoveryEngine();

		new ServiceContextConfigurator().addEngine(engine);

	}
	
	@After
	public void tearDown(){
		bootstrap.stop();
	}
	
	public WiringContext getWriringContext(){
		return  ServiceRegistry.getService(WiringService.class).getWiringContext();
	}
}

package org.middleheaven.tool.test;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.activation.SetActivatorScanner;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;

public abstract class MiddleHeavenTestCase {

	private TestBootstrap bootstrap = new TestBootstrap(new File("."));
	
	@Before
	public final void setUp(){
		bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));
		
		SetActivatorScanner scanner = new SetActivatorScanner();
		configurateTest(scanner);
		
		getWiringService().addActivatorScanner(scanner);
	}
	
	
	protected void configurateTest(SetActivatorScanner scanner) {

	}


	@After
	public void tearDown(){
		bootstrap.stop();
	}
	
	public WiringService getWiringService(){
		return  ServiceRegistry.getService(WiringService.class);
	}
}

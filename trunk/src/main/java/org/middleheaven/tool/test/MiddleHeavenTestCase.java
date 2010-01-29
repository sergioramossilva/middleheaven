package org.middleheaven.tool.test;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.activation.SetActivatorScanner;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.transactions.TestTransactionServiceActivator;

public abstract class MiddleHeavenTestCase {

	private TestBootstrap bootstrap = new TestBootstrap(new File("."));
	
	@Before
	public final void setUp(){
		bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));
		
		SetActivatorScanner scanner = new SetActivatorScanner();
		scanner.addActivator(TestTransactionServiceActivator.class);
		
		configurateActivators(scanner);
		
		final WiringService wiringService = getWiringService();
		wiringService.addActivatorScanner(scanner);
		
	
		wiringService.scan();
		
		wiringService.getObjectPool().wireMembers(this);
		configurateTest();
	}
	
	@After
	public void tearDown(){
		bootstrap.stop();
	}
	
	protected void configurateTest() {

	}
	
	protected void configurateActivators(SetActivatorScanner scanner) {

	}



	
	public WiringService getWiringService(){
		return  ServiceRegistry.getService(WiringService.class);
	}
}

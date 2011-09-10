package org.middleheaven.tool.test;

import org.junit.After;
import org.junit.Before;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.activation.SetActivatorScanner;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;

public abstract class MiddleHeavenTestCase {

	private TestBootstrap bootstrap = new TestBootstrap();

	@Before
	public final void setUp(){
		bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));
		
		SetActivatorScanner scanner = new SetActivatorScanner();
	
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
	
	/**
	 * Override to configure activators needed for the test.
	 * @param scannerSet a SetActivatorScanner to add the necessary activators.
	 */
	protected void configurateActivators(SetActivatorScanner scannerSet){
		
	}

	public WiringService getWiringService(){
		return  ServiceRegistry.getService(WiringService.class);
	}
}

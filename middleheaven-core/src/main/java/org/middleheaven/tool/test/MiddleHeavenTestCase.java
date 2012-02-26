package org.middleheaven.tool.test;

import org.junit.After;
import org.junit.Before;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.activation.SetActivatorScanner;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;

public abstract class MiddleHeavenTestCase {

	private TestBootstrap bootstrap;
	
	@Before
	public final void setUp(){
		bootstrap = new TestBootstrap();
		
		SetActivatorScanner scanner = new SetActivatorScanner();
		
		configurateActivators(scanner);
		
		bootstrap.addActivatorScanner(scanner);
		
		bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));
		
		bootstrap.getWiringService().getObjectPool().wireMembers(this);
		
		configurateTest();
	}
	
	public WiringService getWiringService(){
		return  bootstrap.getWiringService();
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

	
}

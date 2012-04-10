package org.middleheaven.tool.test;

import org.junit.After;
import org.junit.Before;
import org.middleheaven.core.wiring.TypeWiringItem;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.annotations.Component;

@Component
public abstract class MiddleHeavenTestCase extends TestBootstrap {


	@Before
	public final void setUp(){
	
		getWiringService().addItem(new TypeWiringItem(this.getClass()));
		
		setupWiringBundles(getWiringService());

		
		this.start();
		
		getWiringService().wireMembers(this);
		
		configurateTest();
	}

	
	@After
	public void tearDown(){
		this.stop();
	}
	
	protected void configurateTest() {

	}
	
	/**
	 * Override to configure activators needed for the test.
	 * @param classSet a SetActivatorScanner to add the necessary activators.
	 */
	protected void setupWiringBundles(WiringService service){
		
	}

	
}

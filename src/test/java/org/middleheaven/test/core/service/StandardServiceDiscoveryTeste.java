package org.middleheaven.test.core.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.middleheaven.core.services.ServiceContextConfigurator;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.services.engine.ActivatorBagServiceDiscoveryEngine;
import org.middleheaven.core.services.engine.LocalFileRepositoryDiscoveryEngine;
import org.middleheaven.logging.LoggingService;
import org.middleheaven.tool.test.MiddleHeavenTestCase;


public class StandardServiceDiscoveryTeste extends MiddleHeavenTestCase{


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

package org.middleheaven.injection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.middleheaven.core.services.ServiceContextConfigurator;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.services.engine.ActivatorBagServiceDiscoveryEngine;
import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.DefaultWiringService;
import org.middleheaven.core.wiring.Shared;
import org.middleheaven.core.wiring.WiringContext;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.core.wiring.service.ServiceScope;
import org.middleheaven.injection.mock.CyclicDisplayer;
import org.middleheaven.injection.mock.Displayer;
import org.middleheaven.injection.mock.Greeter;
import org.middleheaven.injection.mock.HelloMessage;
import org.middleheaven.injection.mock.Message;
import org.middleheaven.injection.mock.MockDisplay;

public class InjectorTest {

	WiringService service = new DefaultWiringService();
	
	@Before
	public void setUp(){
		new ServiceContextConfigurator().addEngine(new ActivatorBagServiceDiscoveryEngine());
		
		ServiceRegistry.register(WiringService.class, new DefaultWiringService());
		service =ServiceRegistry.getService(WiringService.class);
	}
	
	@After
	public void tearDown(){
		service= null;
	}
	
	@Test
	public void simpleTest(){
		final MockDisplay md = new MockDisplay();
		
		service.getWiringContext()
		.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(Displayer.class).toInstance(md);
				binder.bind(Message.class).to(HelloMessage.class);
				binder.bindProperty(String.class).named("hello.message").toInstance("Hello");
				binder.bindProperty(String.class).named("hello.name").toInstance("World");
			}
			
		})
		.getInstance(Greeter.class)
		.sayHello();
		
		assertEquals("Hello, World", md.getSaing());
	}
	
	@Test
	public void serviceTest(){
		WiringService inj = service.getWiringContext()
		.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bindScope(Service.class , ServiceScope.class);
				binder.bind(WiringService.class).in(Service.class);
			
			}
			
		})
		.getInstance(WiringService.class);
		
		assertTrue(inj!=null);
		assertTrue(inj instanceof WiringService);
		
		WiringContext i = inj.getWiringContext();

	}
	
	@Test
	public void sharedInstanceTest(){
		final MockDisplay md = new MockDisplay();
		
		WiringContext inj = service.getWiringContext()
		.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(Greeter.class).to(Greeter.class).in(Shared.class);
				binder.bind(Displayer.class).toInstance(md);
				binder.bind(Message.class).to(HelloMessage.class);
				binder.bindProperty(String.class).named("hello.message").toInstance("Hello");
				binder.bindProperty(String.class).named("hello.name").toInstance("World");
			}
			
		});
		
		Greeter g1 = inj.getInstance(Greeter.class);
		Greeter g2 = inj.getInstance(Greeter.class);
		
		assertTrue(g1==g2);
	}
	
	@Test
	public void cyclicTest(){

		WiringContext inj = service.getWiringContext()
		.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(Greeter.class).to(Greeter.class);
				binder.bind(Displayer.class).to(CyclicDisplayer.class);
				binder.bind(Message.class).to(HelloMessage.class);
				binder.bindProperty(String.class).named("hello.message").toInstance("Hello");
				binder.bindProperty(String.class).named("hello.name").toInstance("World");
			}
			
		});
		
		Greeter g = inj.getInstance(Greeter.class);
		g.sayHello();
	}
	
	@Test
	public void cyclicSharedTest(){

		WiringContext inj = service.getWiringContext()
		.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(Greeter.class).to(Greeter.class).in(Shared.class);
				binder.bind(Displayer.class).to(CyclicDisplayer.class);
				binder.bind(Message.class).to(HelloMessage.class);
				binder.bindProperty(String.class).named("hello.message").toInstance("Hello");
				binder.bindProperty(String.class).named("hello.name").toInstance("World");
			}
			
		});
		
		Greeter g = inj.getInstance(Greeter.class);
		g.sayHello();
		
	}
}

package org.middleheaven.injection;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.StandaloneBootstrap;
import org.middleheaven.core.bootstrap.client.DesktopUIContainer;
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
import org.middleheaven.io.repository.ManagedFileRepositories;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;

public class InjectorTest {

	
	@BeforeClass
	public static void setUp(){
		Container container = new DesktopUIContainer(ManagedFileRepositories.resolveFile(new File(".")));
		StandaloneBootstrap bootstrap = new StandaloneBootstrap(container);
		bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));
		ActivatorBagServiceDiscoveryEngine engine = new ActivatorBagServiceDiscoveryEngine();

		new ServiceContextConfigurator().addEngine(engine);
		
		WiringService service =ServiceRegistry.getService(WiringService.class);
	}
	
	@After
	public void tearDown(){
		
	}
	
	@Test
	public void simpleTest(){
		final MockDisplay md = new MockDisplay();
		
		ServiceRegistry.getService(WiringService.class).getWiringContext()
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
		
		// obtain the service it self from wiring
		
		WiringContext context = ServiceRegistry.getService(WiringService.class).getWiringContext()
		.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bindScope(Service.class , ServiceScope.class);
				binder.bind(WiringService.class).in(Service.class);
			
			}
			
		});
		
		WiringService inj1 =  context.getInstance(WiringService.class);
		
		assertNotNull(inj1);

		// the service is shared so its the same also
		WiringService inj2 =  context.getInstance(WiringService.class);
		
		assertSame(inj1, inj2);
		
		// the context is the same
		WiringContext context2 = inj1.getWiringContext();
		
		assertSame(context, context2);
		
		
	}
	
	@Test
	public void sharedInstanceTest(){
		final MockDisplay md = new MockDisplay();
		
		WiringContext inj = ServiceRegistry.getService(WiringService.class).getWiringContext()
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

		WiringContext inj = ServiceRegistry.getService(WiringService.class).getWiringContext()
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

		WiringContext inj = ServiceRegistry.getService(WiringService.class).getWiringContext()
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

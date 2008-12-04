package org.middleheaven.test.wiring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.Shared;
import org.middleheaven.core.wiring.WiringContext;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.core.wiring.service.ServiceScope;
import org.middleheaven.test.wiring.mock.CyclicDisplayer;
import org.middleheaven.test.wiring.mock.DictionaryService;
import org.middleheaven.test.wiring.mock.Displayer;
import org.middleheaven.test.wiring.mock.Greeter;
import org.middleheaven.test.wiring.mock.HashDictionaryService;
import org.middleheaven.test.wiring.mock.HelloMessage;
import org.middleheaven.test.wiring.mock.Message;
import org.middleheaven.test.wiring.mock.MockDisplay;
import org.middleheaven.tool.test.MiddleHeavenTestCase;
import org.middleheaven.util.ParamsMap;

public class WiringTestCase extends MiddleHeavenTestCase {

	
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
		
		WiringContext context = this.getWriringContext()
		.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bindScope(Service.class , ServiceScope.class);
				binder.bind(WiringService.class).in(Service.class);
			
			}
			
		});
		
		WiringService inj1 =  context.getInstance(WiringService.class);
		
		assertNotNull(inj1);

		// the service is shared so its the same always
		WiringService inj2 =  context.getInstance(WiringService.class);
		
		assertSame(inj1 , inj2);
		
		// the context is also the same
		WiringContext context2 = inj1.getWiringContext();
		
		assertSame(context, context2);
		
		
	}
	
	@Test
	public void sharedInstanceTest(){
		final MockDisplay md = new MockDisplay();
		
		WiringContext inj = this.getWriringContext()
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

		WiringContext inj = this.getWriringContext()
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

		WiringContext inj = this.getWriringContext()
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
	
	@Test
	public void serviceWiring(){
		

		WiringContext inj = this.getWriringContext()
		.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(DictionaryService.class).in(Service.class);
				binder.bindScope(Service.class, ServiceScope.class);
			}
			
		});
		

		ServiceContext serviceContext = inj.getInstance(ServiceContext.class);
		
		ParamsMap paramsEn = new ParamsMap();
		paramsEn.put("lang", "en");
		serviceContext.register(DictionaryService.class, new HashDictionaryService("en"),paramsEn);
		
		ParamsMap paramsPT = new ParamsMap();
		paramsPT.put("lang", "pt");
		serviceContext.register(DictionaryService.class, new HashDictionaryService("pt"),paramsPT);
		
		
		DictionaryService enDic = inj.getInstance(DictionaryService.class, paramsEn);
		
		assertEquals("en", enDic.getLang());
		
		DictionaryService ptDic = inj.getInstance(DictionaryService.class, paramsPT);
		
		assertEquals("pt", ptDic.getLang());
		
		// with no params the service scope will choose one of the existing implementations
		DictionaryService eDic = inj.getInstance(DictionaryService.class);
		
		assertNotNull(eDic);
		
	}
}

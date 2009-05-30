package org.middleheaven.core.wiring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.annotations.Shared;
import org.middleheaven.core.wiring.mock.C;
import org.middleheaven.core.wiring.mock.CyclicDisplayer;
import org.middleheaven.core.wiring.mock.DictionaryService;
import org.middleheaven.core.wiring.mock.Displayer;
import org.middleheaven.core.wiring.mock.Greeter;
import org.middleheaven.core.wiring.mock.HashDictionaryService;
import org.middleheaven.core.wiring.mock.HelloMessage;
import org.middleheaven.core.wiring.mock.Message;
import org.middleheaven.core.wiring.mock.MockDisplay;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.core.wiring.service.ServiceScope;
import org.middleheaven.mail.MailSendingService;
import org.middleheaven.mail.service.JavaMailSendingService;
import org.middleheaven.tool.test.MiddleHeavenTestCase;
import org.middleheaven.util.collections.ParamsMap;

public class WiringTestCase extends MiddleHeavenTestCase {


	
	@Test
	public void testInheritanceWiring(){
		ObjectPool ctx = ServiceRegistry.getService(WiringService.class).getObjectPool();
		
		C c = ctx.getInstance(C.class);
		
		assertNotNull(c.getX());
	}
	
	
	@Test
	public void simpleTest(){
		final MockDisplay md = new MockDisplay();
		
		ServiceRegistry.getService(WiringService.class).getObjectPool()
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
	public void testWiringServiceWithParams(){
		

		ObjectPool inj = this.getWiringService().getObjectPool()
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
	
	@Test
	public void serviceTest(){
		
		// obtain the service it self from wiring
		
		ObjectPool context = this.getWiringService().getObjectPool();
		
		WiringService inj1 =  context.getInstance(WiringService.class);
		
		assertNotNull(inj1);

		// the service is shared so its the same always
		WiringService inj2 =  context.getInstance(WiringService.class);
		
		assertSame(inj1 , inj2);
		
		// the context is also the same
		ObjectPool context2 = inj1.getObjectPool();
		
		assertSame(context, context2);
		
		
	}
	
	@Test
	public void sharedInstanceTest(){
		final MockDisplay md = new MockDisplay();
		
		ObjectPool inj = this.getWiringService().getObjectPool()
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

		ObjectPool inj = this.getWiringService().getObjectPool()
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

		ObjectPool inj = this.getWiringService().getObjectPool()
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

package org.middleheaven.core.wiring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.annotations.Shared;
import org.middleheaven.core.wiring.mock.C;
import org.middleheaven.core.wiring.mock.CyclicDisplayer;
import org.middleheaven.core.wiring.mock.DictionaryService;
import org.middleheaven.core.wiring.mock.DictionaryUser;
import org.middleheaven.core.wiring.mock.Displayer;
import org.middleheaven.core.wiring.mock.GermanDictionayService;
import org.middleheaven.core.wiring.mock.Greeter;
import org.middleheaven.core.wiring.mock.HashDictionaryService;
import org.middleheaven.core.wiring.mock.HelloMessage;
import org.middleheaven.core.wiring.mock.Message;
import org.middleheaven.core.wiring.mock.MockDisplay;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.core.wiring.service.ServiceScope;
import org.middleheaven.tool.test.MiddleHeavenTestCase;
import org.middleheaven.util.collections.ParamsMap;

public class WiringTestCase extends MiddleHeavenTestCase {

	@Test
	public void testWiringModelReader(){
		DefaultWiringModelParser  parser = new DefaultWiringModelParser();
		
		BeanModel model = new BeanModel(C.class);
		parser.readBeanModel(C.class, model);
		
		assertNotNull(model.getProducingWiringPoint());
		assertNotNull(model.getAfterPoints());
		
		assertFalse("no after points", model.getAfterPoints().isEmpty());
	}
	
	
	@Test
	public void testInheritanceWiring(){
		ObjectPool pool = ServiceRegistry.getService(WiringService.class).getObjectPool();
		
		C c = pool.getInstance(C.class);
		
		assertNotNull(c);
		assertNotNull(c.getX());
	}
	
	
	@Test
	public void testPropertiesWiring(){
		final MockDisplay md = new MockDisplay();
		
		ServiceRegistry.getService(WiringService.class).getObjectPool()
		.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(Displayer.class).toInstance(md);
				binder.bind(Message.class).to(HelloMessage.class);
				binder.bindProperty(String.class).labeled("hello.message").toInstance("Hello");
				binder.bindProperty(String.class).labeled("hello.name").toInstance("World");
			}
			
		})
		.getInstance(Greeter.class)
		.sayHello();
		
		assertEquals("Hello, World", md.getSaing());
	}
	
	@Test
	public void testWiringServiceWithParams(){
		
		final HashDictionaryService en = new HashDictionaryService("en");
		final HashDictionaryService pt = new HashDictionaryService("pt");
		
		
		
		final WiringService wiringService = this.getWiringService();

		
		ObjectPool pool = wiringService.getObjectPool()
		.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(DictionaryService.class).toInstance(en).named("en").in(Service.class);
				binder.bind(DictionaryService.class).toInstance(pt).named("pt").in(Service.class);
				binder.bind(DictionaryService.class).to(GermanDictionayService.class).in(Service.class);
				binder.bind(DictionaryUser.class).to(DictionaryUser.class);
			}
			
		});

		
		DefaultWiringModelParser  parser = new DefaultWiringModelParser();
		
		BeanModel model = new BeanModel(GermanDictionayService.class);
		parser.readBeanModel(GermanDictionayService.class, model);
		
		
		assertFalse("object is not qualified" , model.getParams().isEmpty());
		
		GermanDictionayService ge = pool.getInstance(GermanDictionayService.class);
		assertNotNull(ge);
		
		
		ParamsMap params = new ParamsMap().setParam("name", "en");
		
		DictionaryService eDic = pool.getInstance(DictionaryService.class, params);
		
		assertNotNull(eDic);
		assertEquals("en", eDic.getLang());
		
		
		
		
		
		model = new BeanModel(DictionaryUser.class);
		parser.readBeanModel(DictionaryUser.class, model);
		
		
		assertNotNull(model.getProducingWiringPoint());
		assertNotNull(model.getProducingWiringPoint().getParamsSpecifications()[0]);
		assertFalse("construtor injection point as no params", model.getProducingWiringPoint().getParamsSpecifications()[0].getParams().isEmpty());
		
		DictionaryUser user = pool.getInstance(DictionaryUser.class);
		
		assertNotNull(user);
		assertEquals("ge", user.getLang());
		
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
				binder.bindProperty(String.class).labeled("hello.message").toInstance("Hello");
				binder.bindProperty(String.class).labeled("hello.name").toInstance("World");
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
				binder.bindProperty(String.class).labeled("hello.message").toInstance("Hello");
				binder.bindProperty(String.class).labeled("hello.name").toInstance("World");
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
				binder.bindProperty(String.class).labeled("hello.message").toInstance("Hello");
				binder.bindProperty(String.class).labeled("hello.name").toInstance("World");
			}
			
		});
		
		Greeter g = inj.getInstance(Greeter.class);
		g.sayHello();
		
	}
	
	
}

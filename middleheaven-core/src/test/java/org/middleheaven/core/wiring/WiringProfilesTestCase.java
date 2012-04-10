package org.middleheaven.core.wiring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.annotations.Shared;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.core.wiring.mock.C;
import org.middleheaven.core.wiring.mock.CyclicDisplayer;
import org.middleheaven.core.wiring.mock.DictionaryService;
import org.middleheaven.core.wiring.mock.DictionaryUser;
import org.middleheaven.core.wiring.mock.Displayer;
import org.middleheaven.core.wiring.mock.GermanDictionayService;
import org.middleheaven.core.wiring.mock.GreekDictionayService;
import org.middleheaven.core.wiring.mock.Greeter;
import org.middleheaven.core.wiring.mock.HashDictionaryService;
import org.middleheaven.core.wiring.mock.HelloMessage;
import org.middleheaven.core.wiring.mock.Message;
import org.middleheaven.core.wiring.mock.MockDisplay;
import org.middleheaven.core.wiring.mock.X;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.logging.Logger;
import org.middleheaven.tool.test.MiddleHeavenTestCase;
import org.middleheaven.util.collections.ParamsMap;

public class WiringProfilesTestCase extends MiddleHeavenTestCase {

	@Wire Logger log;
	@Wire WiringService wiringService;

	protected void setupWiringBundles(WiringService wiringService){

		wiringService.getActiveProfiles().add("dev");

		wiringService.addConfiguration(new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				binder.bind(DictionaryService.class).in(Service.class).to(GreekDictionayService.class);
				binder.bind(DictionaryService.class).in(Service.class).to(GermanDictionayService.class);
			}

		});

		
	
	}

	@Test
	public void testWiringServiceWithProfileDev(){


		final WiringService wiringService = this.getWiringService();

		

		DictionaryService eDic = wiringService.getInstance(DictionaryService.class);

		assertEquals("ge", eDic.getLang());


	}
	


}

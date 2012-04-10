/**
 * 
 */
package org.middleheaven.ui.wiring;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.annotations.Shared;
import org.middleheaven.tool.test.MiddleHeavenTestCase;
import org.middleheaven.ui.UIService;
import org.middleheaven.ui.desktop.service.DesktopUIServiceActivator;


/**
 * 
 */
public class ActivatorWiringTest extends MiddleHeavenTestCase{


	@Test
	public void testInit(){
		
		final WiringService wiringService = this.getWiringService();
		
		wiringService.addConfiguration(new BindConfiguration (){

			@Override
			public void configure(Binder binder) {
				binder.bind(DesktopUIServiceActivator.class).in(Shared.class).to(DesktopUIServiceActivator.class);
			}

		});
		
		DesktopUIServiceActivator instance = wiringService.getInstance(DesktopUIServiceActivator.class);
		UIService service = wiringService.getInstance(UIService.class);
		
		assertNotNull(instance);
		assertNotNull(service);
	}
}

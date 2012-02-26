/**
 * 
 */
package org.middleheaven.ui.wiring;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.ObjectPool;
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
		
		final ObjectPool objectPool = this.getWiringService().getObjectPool();
		objectPool.addConfiguration(new BindConfiguration (){

			@Override
			public void configure(Binder binder) {
				binder.bind(DesktopUIServiceActivator.class).to(DesktopUIServiceActivator.class).in(Shared.class);
			}

		});
		
		DesktopUIServiceActivator instance = objectPool.getInstance(DesktopUIServiceActivator.class);
		UIService service = objectPool.getInstance(UIService.class);
		
		assertNotNull(instance);
		assertNotNull(service);
	}
}

/**
 * 
 */
package org.middleheaven.mail;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.annotations.Shared;
import org.middleheaven.mail.service.JavaMailActivator;
import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.namedirectory.jndi.JNDINameDirectoryService;
import org.middleheaven.tool.test.MiddleHeavenTestCase;


/**
 * 
 */
public class ActivatorWiringTest extends MiddleHeavenTestCase{

	
	
	@Test
	public void testInit(){
		
		this.getWiringService().getObjectPool().addConfiguration(new BindConfiguration (){

			@Override
			public void configure(Binder binder) {
				binder.bind(NameDirectoryService.class).to(JNDINameDirectoryService.class).in(Shared.class);
				binder.bind(JavaMailActivator.class).to(JavaMailActivator.class).in(Shared.class);
			}

		});
		
		JavaMailActivator instance = this.getWiringService().getObjectPool().getInstance(JavaMailActivator.class);
		
		assertNotNull(instance);
		
	}
}

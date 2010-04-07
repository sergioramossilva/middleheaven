package org.middleheaven.core.reflection;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.mail.service.NameDirectoryMailSessionSendingService;


public class TestIntrospector {

	
	@Test
	public void testInterfaces(){
		
		Class<?>[] i = Introspector.of(NameDirectoryMailSessionSendingService.class).getImplementedInterfaces();
		
		assertEquals(0, i.length);
		
		i = Introspector.of(NameDirectoryMailSessionSendingService.class).getDeclaredInterfaces();
		
		assertEquals(1, i.length);
	}
	
	
}

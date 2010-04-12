package org.middleheaven.core.reflection;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.mail.service.NameDirectoryMailSessionSendingService;
import org.middleheaven.transactions.Transactional;
import org.middleheaven.util.collections.EnhancedCollection;


public class TestIntrospector {

	
	@Test
	public void testInterfaces(){
		
		Class<?>[] i = Introspector.of(NameDirectoryMailSessionSendingService.class).getImplementedInterfaces();
		
		assertEquals(0, i.length);
		
		i = Introspector.of(NameDirectoryMailSessionSendingService.class).getDeclaredInterfaces();
		
		assertEquals(1, i.length);
	}
	
	
	@Test
	public void testAnnotationsInParents(){
		
		EnhancedCollection<Method> all = Introspector.of(SomeObject.class).inspect()
		.methods()
		.notInheritFromObject()
		.annotatedWith(Wire.class).retriveAll();
		
		assertFalse(all.isEmpty());
	}
}

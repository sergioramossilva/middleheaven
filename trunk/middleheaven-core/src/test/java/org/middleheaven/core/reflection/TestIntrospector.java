package org.middleheaven.core.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.lang.reflect.Method;

import org.junit.Test;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.util.collections.EnhancedCollection;


public class TestIntrospector {

	
	@Test
	public void testInterfaces(){
		
		Class<?>[] i = Introspector.of(SomeObject.class).getImplementedInterfaces();
		
		assertEquals(1, i.length);
		
		i = Introspector.of(SomeObject.class).getDeclaredInterfaces();
		
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

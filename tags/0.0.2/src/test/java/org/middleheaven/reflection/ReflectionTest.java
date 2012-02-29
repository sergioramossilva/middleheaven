package org.middleheaven.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;
import org.middleheaven.core.reflection.ClassCastReflectionException;
import org.middleheaven.core.reflection.MemberAccess;
import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.ProxyHandler;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.util.collections.EnhancedCollection;
import org.middleheaven.util.collections.EnhancedList;


public class ReflectionTest {

	
	@Test
	public void testInstrospectionLoad(){
		
		ClassIntrospector<CharSequence> type = Introspector.of(CharSequence.class).load(String.class.getName());
		
		CharSequence s = type.newInstance();
		
		assertTrue(s instanceof String);
	}
	
	@Test(expected=ClassCastReflectionException.class)
	public void testInstrospectionIncorrectLoad(){
		
		// cannot load a charsequence as a string
		Introspector.of(String.class).load(CharSequence.class.getName());
		
	}

	@Test
	public void testInstropection(){
		
		EnhancedList<Constructor<ArrayList>> c = Introspector.of(ArrayList.class)
		.inspect().constructors().sortedByQuantityOfParameters()
		.withAccess(MemberAccess.PUBLIC)
		.retriveAll().asList();
		
		assertEquals(3 , c.size());
		assertTrue( c.get(0).getParameterTypes().length <= c.get(1).getParameterTypes().length);
		assertTrue( c.get(1).getParameterTypes().length <= c.get(2).getParameterTypes().length);
		
		EnhancedCollection<Method> m = Introspector.of(ArrayList.class)
		.inspect().methods()
		.notInheritFromObject()
		.withAccess(MemberAccess.PUBLIC , MemberAccess.PROTECTED)
		.retriveAll();
		
		assertEquals(34,m.size());
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testwrongProxyClass(){
		
		Cloneable obj = Introspector.of(Date.class).newProxyInstance(new TestMethodHandler(), Cloneable.class);

		assertTrue(obj instanceof Cloneable);
		assertTrue(obj instanceof Date);
	}
	
	@Test
	public void testProxy(){
		
		Cloneable obj = Introspector.of(CharSequence.class).newProxyInstance(new TestMethodHandler(), Cloneable.class);

		assertTrue(obj instanceof Cloneable);
		assertTrue(obj instanceof CharSequence);
	}
	
	public class TestMethodHandler  implements ProxyHandler {
		

		@Override
		public Object invoke(Object self, Object[] args,
				MethodDelegator delegator) throws Throwable {
			System.out.println("Name: " + delegator.getName());
			return delegator.invokeSuper(self, args);  // execute the original method.
		}
	}
}
package org.middleheaven.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.middleheaven.core.reflection.Introspector;
import org.middleheaven.core.reflection.MemberAccess;
import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.ProxyHandler;
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;
import org.middleheaven.util.collections.EnhancedList;


public class ReflectionTest {


	@Test
	public void testInstropection(){
		
		EnhancedList<Constructor<ArrayList>> c = Introspector.of(ArrayList.class)
		.inspect().constructors().sortedByQuantityOfParameters()
		.withAccess(MemberAccess.PUBLIC)
		.retrive().asList();
		
		assertEquals(3 , c.size());
		assertTrue( c.get(0).getParameterTypes().length <= c.get(1).getParameterTypes().length);
		assertTrue( c.get(1).getParameterTypes().length <= c.get(2).getParameterTypes().length);
		
		EnhancedCollection<Method> m = Introspector.of(ArrayList.class)
		.inspect().methods()
		.notInheritFromObject()
		.withAccess(MemberAccess.PUBLIC , MemberAccess.PROTECTED)
		.retrive();
		
		assertEquals(34,m.size());
	}
	
	@Test
	public void testProxy(){
		
		
		Object obj = ReflectionUtils.proxy(new Date() , new TestMethodHandler() , Cloneable.class);
		
		assertTrue(obj instanceof Cloneable);
		assertTrue(obj instanceof Date);
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

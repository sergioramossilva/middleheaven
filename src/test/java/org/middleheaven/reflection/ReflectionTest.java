package org.middleheaven.reflection;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.ProxyHandler;
import org.middleheaven.core.reflection.ReflectionUtils;


public class ReflectionTest {



	
	@Test
	public void testProxy(){
		
		
		Object obj = ReflectionUtils.proxy(new Date() , Cloneable.class , new TestMethodHandler());
		
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

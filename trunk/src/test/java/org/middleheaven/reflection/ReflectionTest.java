package org.middleheaven.reflection;

import java.lang.reflect.Method;
import java.util.Date;

import javassist.util.proxy.MethodHandler;

import org.junit.Test;
import org.middleheaven.core.reflection.ProxyUtils;


public class ReflectionTest {


	@Test
	public void testProxy(){
		
		
		ProxyUtils.decorate(new Date() , Cloneable.class , new TestMethodHandler());
	}
	
	
	public class TestMethodHandler  implements MethodHandler {
		public Object invoke(Object self, Method m, Method original,Object[] args) throws Throwable {
			System.out.println("Name: " + m.getName());
			return original.invoke(self, args);  // execute the original method.
		}
	}
}

package org.middleheaven.core.reflection;

import java.lang.reflect.Method;

import javassist.util.proxy.MethodHandler;

public class DefaultMethodDelegator implements MethodHandler {
	Object original;
	
	public DefaultMethodDelegator (Object original){
		this.original = original;
	}
	
	public Object invoke(Object self, Method m, Method proceed, Object[] args) throws Throwable {
		System.out.println("Name: " + m.getName());
		
		if (proceed==null){
			// a classe base não tem este método.
			// delega para este delegator
			return this.getClass().getMethod(m.getName(), m.getParameterTypes()).invoke(this, args);
		} else {
			return proceed.invoke(self, args);  // execute the original method.
		}
		
	}

};
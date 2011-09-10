package org.middleheaven.core.reflection;

import java.lang.reflect.Method;

public interface MethodDelegator {
	
	public Method getInvoked();
	public String getName();
	public boolean hasSuper();
	public Object invokeSuper(Object target,Object[] args) throws Throwable;
	public Object invoke(Object target,Object[] args)  throws Throwable;
	
	 
}

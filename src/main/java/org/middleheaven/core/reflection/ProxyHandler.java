package org.middleheaven.core.reflection;


public interface ProxyHandler  {

	public Object invoke(Object proxy,Object[] args, MethodDelegator delegator ) throws Throwable;
	
}

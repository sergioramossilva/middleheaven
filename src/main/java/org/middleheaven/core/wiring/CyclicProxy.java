package org.middleheaven.core.wiring;

import java.lang.reflect.Method;

import org.middleheaven.core.reflection.ProxyHandler;

import javassist.util.proxy.MethodHandler;

public class CyclicProxy implements ProxyHandler{

	private Object realObject;
	
	@Override
	public Object invoke(Object obj, Method method, Method original, Object[] params) throws Throwable {
		if (realObject==null){
			throw new InstantiationException("Cyclic Reference was not resolved");
		} else {
			return method.invoke(realObject,params);
		}
	}

	protected void setRealObject(Object realObject) {
		this.realObject = realObject;
	}

}

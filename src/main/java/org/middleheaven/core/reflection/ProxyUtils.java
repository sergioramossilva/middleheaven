package org.middleheaven.core.reflection;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;



public final class ProxyUtils {

	private ProxyUtils(){}

	public static <T> T proxy (Class<T> facadeClass , MethodHandler delegator){
		try {
			if (facadeClass.isInterface()){
				return (T)Proxy.newProxyInstance(ProxyUtils.class.getClassLoader(),new Class<?>[]{facadeClass}, new MethodInvocationHandler(delegator));
			} else{
				ProxyFactory f = new ProxyFactory();
				f.setSuperclass(facadeClass);
				f.setFilter(new MethodFilter() {
					public boolean isHandled(Method m) {
						// ignore finalize()
						return !m.getName().equals("finalize");
					}
				});
				Constructor[] all = facadeClass.getConstructors();
				Constructor candidate=null;
				for (Constructor c : all){
					if (candidate==null || c.getParameterTypes().length < candidate.getParameterTypes().length ) {
						candidate = c;
					}
				}
				Object[] allNull = new Object[candidate.getParameterTypes().length]; 
				return (T)f.create(candidate.getParameterTypes(), allNull, delegator);

			} 
		} catch (InstantiationException e) {
			throw new InstantiationReflectionException(facadeClass.getName(), e.getMessage());
		} catch (Exception e) {
			new ReflectionExceptionHandler().handle(e);
			return null;
		}
	}


	private static class MethodInvocationHandler implements InvocationHandler{

		private MethodHandler methodHandler;
		private MethodInvocationHandler(MethodHandler methodHandler){
			this.methodHandler =  methodHandler;
		}


		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return methodHandler.invoke(proxy,method,null,args);
		}

	}

	/**
	 * Returns an object of same class as  delegationTarget but that
	 * implements interface T
	 * @param <T>
	 * @param delegationTarget
	 * @param facadeClass interface ouf class
	 * @param delegator Proxy object that intercepts method invocation
	 * @return
	 */
	public static <T> T decorate ( Object delegationTarget , Class<T> facadeClass, MethodHandler delegator){
		if (!facadeClass.isInterface()){
			throw new IllegalArgumentException("Decoration must be applied with an interface");
		}
		ProxyFactory f = new ProxyFactory();
		f.setSuperclass(delegationTarget.getClass());
		f.setInterfaces(new Class[]{facadeClass});
		f.setFilter(new MethodFilter() {
			public boolean isHandled(Method m) {
				// ignore finalize()
				return !m.getName().equals("finalize");
			}
		});

		T foo;
		try {
			Class c = f.createClass();
			foo = (T)c.newInstance();

			((ProxyObject)foo).setHandler(delegator);

			return foo;
		} catch (InstantiationException e) {
			throw new InstantiationReflectionException(delegationTarget.getClass().getName(), e.getMessage());
		} catch (IllegalAccessException e) {
			throw new IllegalAccesReflectionException(e);
		}
	}

	public static <T> T proxyForClass (Class<T> template){
		ProxyFactory f = new ProxyFactory();
		f.setSuperclass(template);
		f.setFilter(new MethodFilter() {
			public boolean isHandled(Method m) {
				// ignore finalize()
				return !m.getName().equals("finalize");
			}
		});
		Class<?> c = f.createClass();
		T foo;
		try {
			foo = (T)c.newInstance();
			MethodHandler mi = new OriginalMethodHandler();

			((ProxyObject)foo).setHandler(mi);

			return foo;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	};

}

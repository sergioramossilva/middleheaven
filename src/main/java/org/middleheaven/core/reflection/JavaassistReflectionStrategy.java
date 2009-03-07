package org.middleheaven.core.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

public class JavaassistReflectionStrategy extends AbstractReflectionStrategy {

	private static class MethodInvocationHandler implements InvocationHandler{

		private ProxyHandler methodHandler;
		private MethodInvocationHandler(ProxyHandler methodHandler){
			this.methodHandler =  methodHandler;
		}


		@Override
		public Object invoke(Object proxy, final Method method, Object[] args) throws Throwable {
			return methodHandler.invoke(proxy,args , new MethodDelegator(){

				@Override
				public Method getInvoked() {
					return method;
				}

				@Override
				public String getName() {
					return method.getName();
				}

				@Override
				public boolean hasSuper() {
					return false;
				}

				@Override
				public Object invoke(Object target, Object[] args) throws Throwable {
					return method.invoke(target, args);
				}

				@Override
				public Object invokeSuper(Object target, Object[] args) throws Throwable {
					throw new ReflectionException("No such method in super class");
				}
				
			});
		}

	}
	
	private static class JavaAssistMethodHandlder implements MethodHandler{

		ProxyHandler handler;
		public JavaAssistMethodHandlder(ProxyHandler handler){
			this.handler = handler;
		}
		
		@Override
		public Object invoke(Object proxy, final Method invoked, final Method original, Object[] args) throws Throwable {
			return handler.invoke(proxy, args , new MethodDelegator(){

				@Override
				public Method getInvoked() {
					return invoked;
				}

				@Override
				public String getName() {
					return invoked.getName();
				}

				@Override
				public boolean hasSuper() {
					return original!=null;
				}

				@Override
				public Object invoke(Object target, Object[] args)
						throws Throwable {
					return invoked.invoke(target, args);
				}

				@Override
				public Object invokeSuper(Object target,
						Object[] args) throws Throwable {
					if (original==null){
						throw new ReflectionException("No such method in super class");
					}
					return original.invoke(target, args);
				}
				
			});
		}
		
	}
	
	
	
	
	public <I> I proxy (Object delegationTarget , Class<I> proxyInterface , final ProxyHandler handler ){
		if (!proxyInterface.isInterface()){
			throw new IllegalArgumentException("Proxy must be applied with an interface");
		}

		if (proxyInterface.isInstance(delegationTarget)){
			// the object already implements the interface. just wrapp it
			return proxyInterface.cast(Proxy.newProxyInstance(delegationTarget.getClass().getClassLoader(), new Class[]{proxyInterface}, new InvocationHandler(){

				@Override
				public Object invoke(Object proxy, final Method method, Object[] args) throws Throwable {
					return handler.invoke(proxy, args , new MethodDelegator(){

						@Override
						public Method getInvoked() {
							return method;
						}

						@Override
						public String getName() {
							return method.getName();
						}

						@Override
						public boolean hasSuper() {
							return false;
						}

						@Override
						public Object invoke(Object target, Object[] args) throws Throwable {
							return method.invoke(target, args);
						}

						@Override
						public Object invokeSuper(Object target, Object[] args) throws Throwable {
							throw new NoSuchMethodReflectionException("No such method " + method.getName() + " in super class");
						}
						
					});
				}
				
			}));
			
		} else {
			// delegationTarget does not implement the same interface

			ProxyFactory f = new ProxyFactory();
			f.setSuperclass(delegationTarget.getClass());
			f.setInterfaces(new Class[]{proxyInterface});
			f.setFilter(new MethodFilter() {
				public boolean isHandled(Method m) {
					// ignore finalize()
					return !m.getName().equals("finalize");
				}
			});

			I foo;
			try {
				Class c = f.createClass();
				foo = (I)c.newInstance();

				((ProxyObject)foo).setHandler(new JavaAssistMethodHandlder(handler));

				ReflectionUtils.copy(delegationTarget, foo);
				return foo;
			} catch (InstantiationException e) {
				throw new ReflectionException(e);
			} catch (IllegalAccessException e) {
				throw new ReflectionException(e);
			}
		}
	}

	
	public <I> I proxy (final Object delegationTarget , Class<I> proxyInterface){
		if (!proxyInterface.isInterface()){
			throw new IllegalArgumentException("Proxy must be applied with an interface");
		}
		
		return proxyInterface.cast(Proxy.newProxyInstance(delegationTarget.getClass().getClassLoader(), new Class[]{proxyInterface}, new InvocationHandler(){

			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				try {
					return method.invoke(delegationTarget, args);  // execute the original method.
				} catch (IllegalArgumentException e){
					// try to find a method with the same name and parameters 
					Method m = delegationTarget.getClass().getMethod(method.getName(), method.getParameterTypes());
					if (m!=null && m.getReturnType().isAssignableFrom(method.getReturnType()) ){
						return m.invoke(delegationTarget, args);
					} else {
						throw new NoSuchMethodReflectionException (method.toString());
					}
				}
				
			}
			
		}));
	}
	
	public <T> T proxy (Class<T> facadeClass , final ProxyHandler handler){
		try {
			if (facadeClass.isInterface()){
				return (T)Proxy.newProxyInstance(ReflectionUtils.class.getClassLoader(),new Class<?>[]{facadeClass}, new MethodInvocationHandler(handler));
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
				return (T)f.create(candidate.getParameterTypes(), allNull, new JavaAssistMethodHandlder(handler));

			} 
		} catch (InstantiationException e) {
			throw new InstantiationReflectionException(facadeClass.getName(), e.getMessage());
		} catch (Exception e) {
			throw new ReflectionExceptionHandler().handle(e);
		}
	}


}

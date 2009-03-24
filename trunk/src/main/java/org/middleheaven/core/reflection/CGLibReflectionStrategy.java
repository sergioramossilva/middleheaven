package org.middleheaven.core.reflection;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Dispatcher;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;


public class CGLibReflectionStrategy extends AbstractReflectionStrategy{

	
	private class ProxyMethodDelegator implements MethodDelegator {

		private Class<? extends Object> type;
		private Method invoked;
		private MethodProxy methodProxy;

		public ProxyMethodDelegator(Class<? extends Object> type,Method invoked, MethodProxy methodProxy) {
			this.type = type;
			this.invoked = invoked;
			this.methodProxy= methodProxy;
		}

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
			try{
				type.getMethod(invoked.getName(), invoked.getParameterTypes());
				return true;
			} catch (NoSuchMethodException e){
				return false;
			}
		}

		@Override
		public Object invoke(Object target, Object[] args) throws Throwable {
			return methodProxy.invoke(target, args);
		}

		@Override
		public Object invokeSuper(Object target, Object[] args) throws Throwable {
			return methodProxy.invokeSuper(target, args);
		}
		
	}
	private class ProxyHandlerInterceptor implements MethodInterceptor{

		private ProxyHandler handler;
		private Class<?> originalType;
		
		public ProxyHandlerInterceptor(Class<?> originalType, ProxyHandler handler) {
			super();
			this.originalType = originalType;
			this.handler = handler;
		}

		@Override
		public Object intercept(Object proxy, Method invoked, Object[] args,MethodProxy methodProxy) throws Throwable {
			return handler.invoke(proxy, args, new ProxyMethodDelegator(originalType,invoked,methodProxy));
		}

	}
	

	@Override
	public <T> T proxy(Class<T> facadeClass, ProxyHandler handler) {
		try{
			return facadeClass.cast(Enhancer.create(
					facadeClass,
					new ProxyHandlerInterceptor(facadeClass,handler)
			));
		}catch (RuntimeException e){
			throw new ReflectionException(e);
		}
	}

	@Override
	public <I> I proxy(final Object delegationTarget, Class<I> proxyInterface) {
		if (!proxyInterface.isInterface()){
			throw new IllegalArgumentException("Proxy must be applied with an interface");
		}

		try{
			return proxyInterface.cast(Enhancer.create(
					delegationTarget.getClass(), 
					new Class[]{proxyInterface},
					new Dispatcher(){
						public Object loadObject(){
							return delegationTarget;
						}
					}
			));
		}catch (RuntimeException e){
			throw new ReflectionException(e);
		}
	}

	@Override
	public <I> I proxy(Object delegationTarget, Class<I> proxyInterface,ProxyHandler handler) {
		if (!proxyInterface.isInterface()){
			throw new IllegalArgumentException("Proxy must be applied with an interface");
		}

		if (proxyInterface.isInstance(delegationTarget)){
			return proxy(delegationTarget,proxyInterface);
		} else {
			try{
				return proxyInterface.cast(Enhancer.create(
						delegationTarget.getClass(), 
						new Class[]{proxyInterface},
						new ProxyHandlerInterceptor(delegationTarget.getClass(),handler))
				);
			}catch (RuntimeException e){
				throw new ReflectionException(e);
			}
		}
	


	}

}

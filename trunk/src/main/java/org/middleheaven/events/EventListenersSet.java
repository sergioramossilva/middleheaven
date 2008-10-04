package org.middleheaven.events;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javassist.util.proxy.MethodHandler;

import org.middleheaven.core.reflection.ProxyUtils;

/**
 * 
 * @param <L> Listener class
 */
public class EventListenersSet<L> {

	private final Set<L> listeners = new CopyOnWriteArraySet<L>();
	private L listener;
	
	public static <T> EventListenersSet<T> newSet(Class<T> listenerType){
		EventListenersSet<T> set = new EventListenersSet<T>();
		set.listener  = ProxyUtils.decorate(set, listenerType, new EventMethodHandler(set));
		return set;
	}
	
	protected EventListenersSet(){}
	
	public void addListener ( L listener){
		listeners.add(listener);
	}
	
	public void removeListener(L listener) {
		listeners.remove(listener);
	}
	
	public L broadcastEvent(){
		return listener;
	}

	private static class EventMethodHandler implements MethodHandler{

		Set<?> listeners;
		
		public EventMethodHandler(EventListenersSet<?> set) {
			this.listeners = set.listeners;
		}

		@Override
		public Object invoke(Object obj, Method method, Method method2,Object[] params) throws Throwable {
			for (Object listener: listeners){
				method.invoke(listener, params);
			}
			return null;
		}
		
	}

}

package org.middleheaven.core.wiring;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.wiring.annotations.Default;
import org.middleheaven.events.EventListenersSet;

public final class Binding {

	
	public interface BindingScopeListener {
		
		public void onScopeChange(Binding biding);
		
	}
	
	private Class<?> startType;
	private Class<?> scope = Default.class;
	private Resolver resolver;
	private Map<String, Object> params = new HashMap<String, Object>();
	private boolean lazy = false;
	private boolean inicialized = false;
	
	private EventListenersSet<BindingScopeListener> eventListeners = EventListenersSet.newSet(BindingScopeListener.class);
	
	public Binding(){}

	
	/**
	 * Obtains {@link boolean}.
	 * @return the inicialized
	 */
	public boolean isInicialized() {
		return inicialized;
	}


	/**
	 * Atributes {@link boolean}.
	 * @param inicialized the inicialized to set
	 */
	public void setInicialized(boolean inicialized) {
		this.inicialized = inicialized;
	}


	/**
	 * Obtains {@link boolean}.
	 * @return the lazy
	 */
	public boolean isLazy() {
		return lazy;
	}



	/**
	 * Atributes {@link boolean}.
	 * @param lazy the lazy to set
	 */
	public void setLazy(boolean lazy) {
		this.lazy = lazy;
	}



	public String toString(){
		return startType.getName() + "+" + params.toString();
	}
	
	public Class<?> getAbstractType() {
		return startType;
	}
	
	public void setAbstractType(Class<?> startType) {
		this.startType = startType;
	}
	
	public void setResolver(Resolver<?> resolver) {
		this.resolver = resolver;
	}
	
	public <T> Resolver<T> getResolver() {
		return resolver;
	}

	public Key getKey(){
		return Key.keyFor(this.startType, params);
	}

	public void addParam(String key, Object value){
		this.params.put(key, value);
	}
	
	public Map<String, Object> getParams(){
		return this.params;
	}

	public void setTargetScope(Class<?> scope) {
		this.scope = scope;
		eventListeners.broadcastEvent().onScopeChange(this);
	}
	
	public void addListeners(BindingScopeListener listener){
		this.eventListeners.addListener(listener);
	}
	
	public Class<?> getScope() {
		return scope;
	}

	/**
	 * @param params2
	 */
	public void addParams(Map<String, Object> all) {
		this.params.putAll(all);
	}
}

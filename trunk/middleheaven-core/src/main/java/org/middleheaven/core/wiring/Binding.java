package org.middleheaven.core.wiring;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.events.EventListenersSet;

public final class Binding {

	
	public interface BindingScopeListener {
		
		public void onScopeChange(Binding biding);
		
	}
	
	
	
	private boolean lazy = false;
	private boolean inicialized = false;
	private EventListenersSet<BindingScopeListener> eventListeners = EventListenersSet.newSet(BindingScopeListener.class);
	
	
	private Class<?> sourceType;
	private Map<String, Object> params = new HashMap<String, Object>();
	
	private String scope = "default";
	private Resolver resolver;
	private Provider<?> provider;
	private ProfilesBag profiles = new ProfilesBag();

	
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



	/**
	 * Obtains {@link ProfilesBag}.
	 * @return the profiles
	 */
	public ProfilesBag getProfiles() {
		return profiles;
	}


	/**
	 * Atributes {@link ProfilesBag}.
	 * @param profiles the profiles to set
	 */
	public void setProfiles(ProfilesBag profiles) {
		this.profiles = profiles;
	}


	public String toString(){
		return sourceType.getName() + "#" + params.toString() + "@" + this.profiles;
	}
	
	public Class<?> getSourceType() {
		return sourceType;
	}
	
	public void setSourceType(Class<?> sourceType) {
		this.sourceType = sourceType;
	}
	
	public void setResolver(Resolver resolver) {
		this.resolver = resolver;
	}
	
	public Resolver getResolver() {
		return resolver;
	}

	public Key getKey(){
		return Key.keyFor(this.sourceType, params);
	}

	public void addParam(String key, Object value){
		this.params.put(key, value);
	}
	
	public Map<String, Object> getParams(){
		return this.params;
	}

	public void setScope(String scope) {
		this.scope = scope;
		eventListeners.broadcastEvent().onScopeChange(this);
	}
	
	public void addListeners(BindingScopeListener listener){
		this.eventListeners.addListener(listener);
	}
	
	public String getScope() {
		return scope;
	}

	/**
	 * @param params2
	 */
	public void addParams(Map<String, Object> all) {
		this.params.putAll(all);
	}


	/**
	 * @return
	 */
	public Provider<?> getProvider() {
		return provider;
	}


	/**
	 * Atributes {@link Provider<?>}.
	 * @param provider the provider to set
	 */
	protected void setProvider(Provider<?> provider) {
		this.provider = provider;
	}
	
	
}

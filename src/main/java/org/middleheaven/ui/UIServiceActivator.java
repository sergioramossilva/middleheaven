package org.middleheaven.ui;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.core.services.Publish;
import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;

public class UIServiceActivator extends ServiceActivator {

	SimpleUIService service = new SimpleUIService();
	private Object typeEnvs;
	
	@Publish
	public UIService getUIService(){
		return service;
	}
	
	@Override
	public void activate(ServiceAtivatorContext context) {
		
	}

	@Override
	public void inactivate(ServiceAtivatorContext context) {
		
	}

	
	private class SimpleUIService implements UIService{

		Map<String, UIEnvironment> envs = new TreeMap<String, UIEnvironment>();
		Map<UIEnvironmentType, Collection<UIEnvironment>> typeEnvs = new EnumMap<UIEnvironmentType, Collection<UIEnvironment>>(UIEnvironmentType.class);
		Map<UIEnvironmentType, UIEnvironment> defaultEnvs = new EnumMap<UIEnvironmentType, UIEnvironment>(UIEnvironmentType.class);
		
		@Override
		public UIEnvironment getUIEnvironment(String envID) {
			return envs.get(envID);
		}

		@Override
		public Collection<UIEnvironment> getUIEnvironments(UIEnvironmentType type) {
			return Collections.unmodifiableCollection(getEnvironmentCollection(type));
		}

		@Override
		public void registerEnvironment(UIEnvironment env, boolean isDefault) {
			if (isDefault){
				if (!env.equals(defaultEnvs.get(env.getType()))){
					throw new UIException("Default environment already set for type " + env.getType());
				}
				this.defaultEnvs.put(env.getType(), env);
			}
		
			getEnvironmentCollection(env.getType()).add(env);
		}

		private Collection<UIEnvironment> getEnvironmentCollection(UIEnvironmentType type){
			Collection<UIEnvironment>  list = this.typeEnvs.get(type);
			if (list==null){
				list = new HashSet<UIEnvironment>();
				this.typeEnvs.put(type, list);
			}
			return list;
		}
			
		@Override
		public void unRegisterEnvironment(UIEnvironment env) {
			getEnvironmentCollection(env.getType()).remove(env);
			if (this.defaultEnvs.containsValue(env)){
				this.defaultEnvs.remove(env.getType());
			}
		}

		@Override
		public UIEnvironment getDefaultUIEnvironment(UIEnvironmentType type) {
			UIEnvironment env = this.defaultEnvs.get(type);
			if (env==null){
				if (this.getEnvironmentCollection(type).size()==1){
					env =  this.getEnvironmentCollection(type).iterator().next();
				} else {
					throw new UIException("Default environment for type " + type + " is not defined.");
				}
			} 
			return env;
			
		}

		
		
	}
}

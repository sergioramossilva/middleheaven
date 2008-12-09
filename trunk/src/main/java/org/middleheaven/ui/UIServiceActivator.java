package org.middleheaven.ui;

import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.core.services.Publish;
import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;

public class UIServiceActivator extends ServiceActivator {

	SimpleUIService service = new SimpleUIService();
	
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
		Map<UIEnvironmentType , Map<String, UIClient>> clients = new TreeMap<UIEnvironmentType , Map<String, UIClient>>();
		
		@Override
		public UIEnvironment getUIEnvironment(String envID) {
			return envs.get(envID);
		}

		@Override
		public void registerEnvironment(UIEnvironment env) {
		    envs.put(env.getGID(),env);
		}

		@Override
		public UIClient getUIClient(UIEnvironmentType type, String name) {
			Map<String, UIClient> map = clients.get(type);
			if (map!=null){
				return map.get(name);
			}
			return null;
		}

		@Override
		public void setUIClient(UIEnvironmentType type, String name,
				UIClient client) {
			Map<String, UIClient> map = clients.get(type);
			if (map==null){
				map = new TreeMap<String, UIClient>();
				clients.put(type, map);
			}
			map.put(name, client);
			
		}

		
		
	}
}

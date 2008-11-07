package org.middleheaven.ui;

import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.ui.client.UIClient;

public class UIServiceActivator extends ServiceActivator {

	SimpleUIService service = new SimpleUIService();
	
	@Override
	public void activate(ServiceContext context) {
		context.register(UIService.class, service,null);
	}

	@Override
	public void inactivate(ServiceContext context) {
		context.unRegister(UIService.class, service,null);
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
		    envs.put(env.getID(),env);
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

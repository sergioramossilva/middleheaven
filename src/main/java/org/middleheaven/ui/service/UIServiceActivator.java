package org.middleheaven.ui.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.ui.UIEnvironment;
import org.middleheaven.ui.UIEnvironmentType;
import org.middleheaven.ui.UIException;

public class UIServiceActivator extends Activator {

	SimpleUIService service = new SimpleUIService();
	
	@Publish
	public UIService getUIService(){
		return service;
	}
	
	@Override
	public void activate(ActivationContext context) {
		
	}

	@Override
	public void inactivate(ActivationContext context) {
		
	}

	
	private class SimpleUIService implements UIService{

		Map<String, UIEnvironment> envs = new TreeMap<String, UIEnvironment>();
		Map<UIEnvironmentType , List<UIEnvironment>> environmentTypes = new TreeMap<UIEnvironmentType , List<UIEnvironment>>();
		Map<UIEnvironmentType , UIEnvironment> typeDefaults = new TreeMap<UIEnvironmentType , UIEnvironment>();
		
		@Override
		public UIEnvironment getUIEnvironment(String envID) {
			return envs.get(envID);
		}


		@Override
		public UIEnvironment getDefaultUIEnvironment(UIEnvironmentType type) {
			return typeDefaults.get(type);
		}

		@Override
		public Collection<UIEnvironment> getUIEnvironment(UIEnvironmentType type) {
			return environmentTypes.get(type);
		}

		@Override
		public void registerEnvironment(UIEnvironment env, boolean isTypeDeault) throws UIException {
			
			List<UIEnvironment> list = environmentTypes.get(env.getType());
			if (list==null){
				list = new ArrayList<UIEnvironment>(2);
				environmentTypes.put(env.getType(),list);
			}
			list.add(env);
			
			if(isTypeDeault){
				if (this.typeDefaults.get(env.getType())!=null){
					throw new UIException("Default is already set");
				}
				this.typeDefaults.put(env.getType(), env);
			}
		}


		@Override
		public void unRegisterEnvironment(UIEnvironment env) {
			this.typeDefaults.remove(env.getType());
			this.environmentTypes.remove(env.getType());
		}

		
		
	}
}

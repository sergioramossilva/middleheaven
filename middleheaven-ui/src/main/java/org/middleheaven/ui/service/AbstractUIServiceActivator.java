package org.middleheaven.ui.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.middleheaven.core.bootstrap.activation.ServiceActivator;
import org.middleheaven.core.bootstrap.activation.ServiceSpecification;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.ui.UIEnvironment;
import org.middleheaven.ui.UIEnvironmentType;
import org.middleheaven.ui.UIException;
import org.middleheaven.ui.UIService;

public abstract class AbstractUIServiceActivator extends ServiceActivator {

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		//no-dependencies
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(new ServiceSpecification(UIService.class));
	}
	
	@Override
	public final void activate(ServiceContext serviceContext) {
		
		SimpleUIService service = new SimpleUIService();
		
		registerEnvironment(service);
		
		serviceContext.register(UIService.class, service);
	}
	
	protected abstract void registerEnvironment(UIService uiService);
	
	
	@Override
	public void inactivate(ServiceContext serviceContext) {
		serviceContext.unRegister(UIService.class);
	}

	@Service
	private static class SimpleUIService implements UIService{

		Map<String, UIEnvironment> envs = new HashMap<String, UIEnvironment>();
		Map<UIEnvironmentType , List<UIEnvironment>> environmentTypes = new HashMap<UIEnvironmentType , List<UIEnvironment>>();
		Map<UIEnvironmentType , UIEnvironment> typeDefaults = new HashMap<UIEnvironmentType , UIEnvironment>();
		
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

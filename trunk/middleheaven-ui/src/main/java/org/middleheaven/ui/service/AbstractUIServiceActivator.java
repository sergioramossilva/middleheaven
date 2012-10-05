package org.middleheaven.ui.service;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.process.AttributeContext;
import org.middleheaven.ui.Rendering;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIEnvironment;
import org.middleheaven.ui.UIEnvironmentType;
import org.middleheaven.ui.UIException;
import org.middleheaven.ui.UIService;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * 
 */
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
		
		serviceContext.register(UIService.class, service);
	}
	
	
	@Override
	public void inactivate(ServiceContext serviceContext) {
		serviceContext.unRegister(UIService.class);
	}

	private static class SimpleUIService implements UIService{

		Map<String, UIEnvironmentType> envs = new HashMap<String, UIEnvironmentType>();
		Map<UIEnvironmentType , UIEnvironmentRegisty> environmentTypes = new EnumMap<UIEnvironmentType , UIEnvironmentRegisty >(UIEnvironmentType.class);

		@Override
		public UIEnvironment getUIEnvironment(String envID) {
			return getUIEnvironment(envs.get(envID));
		}

		@Override
		public UIEnvironment getUIEnvironment(UIEnvironmentType type) {
			UIEnvironmentRegisty registry = environmentTypes.get(type);
			
			if (registry == null){
				return null;
			}
			
			return registry.environment;
		}

		@Override
		public void registerEnvironment(UIEnvironment env, RenderKit renderKit, AttributeContext context) throws UIException {
			
			UIEnvironmentRegisty registry = new UIEnvironmentRegisty();
			
			registry.attributeContext = context;
			registry.environment = env;
			registry.renderkit = renderKit;
	
			environmentTypes.put(env.getType(), registry);

		}


		@Override
		public void unRegisterEnvironment(UIEnvironment env) {
			this.envs.remove(env.getName());
			this.environmentTypes.remove(env.getType());
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public Rendering<UIClient> getUIClientRendering(UIEnvironmentType type) {
			
			UIEnvironmentRegisty registry = environmentTypes.get(type);
			
			if (registry == null){
				return null;
			}
			
			if (registry.renderedClient == null) {
				final RenderKit renderKit = registry.renderkit;

				RenderingContext renderedContext = new RenderingContext(registry.attributeContext, renderKit);
				
				registry.renderedClient = renderKit.renderComponent(renderedContext, null, registry.environment.getClient());
				
			}
		
			return registry;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Rendering<UIClient> getUIClientRendering(UIEnvironmentType type, AttributeContext context) {
			UIEnvironmentRegisty registry = environmentTypes.get(type);
			
			if (registry == null){
				return null;
			}
			
			if (registry.renderedClient == null) {
				final RenderKit renderKit = registry.renderkit;

				RenderingContext renderedContext = new RenderingContext(context, renderKit);
				
				registry.renderedClient = renderKit.renderComponent(renderedContext, null, registry.environment.getClient());
				
			}
		
			return registry;
		}

		
		static class  UIEnvironmentRegisty implements Rendering<UIClient>{
			
			 UIEnvironment environment;
			 RenderKit renderkit;
			 AttributeContext attributeContext;
			 UIClient renderedClient;
			/**
			 * {@inheritDoc}
			 */
			@Override
			public UIClient getComponent() {
				return renderedClient;
			}
			/**
			 * {@inheritDoc}
			 */
			@Override
			public RenderKit getRenderKit() {
				return renderkit;
			}
			
		}



		
	}
}

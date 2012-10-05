/**
 * 
 */
package org.middleheaven.application;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.bootstrap.BootstapListener;
import org.middleheaven.core.bootstrap.BootstrapEvent;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.util.Version;

/**
 * Modular Aplication Service implementation.
 */
class ModularApplicationService implements ApplicationService {

	private Map<String, ModularApplicationRegistry> registar = new HashMap<String, ModularApplicationRegistry>();

	private class MyBootstapListener implements BootstapListener {

		ServiceContext serviceContext;
		
		public MyBootstapListener (ServiceContext serviceContext){
			this.serviceContext = serviceContext;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onBoostapEvent(BootstrapEvent event) {
			if(event.isBootup() && event.isEnd()){
				// start cycle
				doStart(serviceContext);
			} else if (event.isBootdown() && event.isStart()){
				// end cycle
				doStop(serviceContext);
			}
		}

	}


	/**
	 * Constructor.
	 * @param serviceContext
	 */
	public ModularApplicationService(ServiceContext serviceContext) {
	
		BootstrapService bootstrapService = serviceContext.getService(BootstrapService.class);

		bootstrapService.addListener(new MyBootstapListener(serviceContext));

	}

	protected void doStart(ServiceContext serviceContext) {

		//TODO this can be paralelized ?
		for (ModularApplicationRegistry registry : this.registar.values()){

			registry.start(serviceContext);

		}

	}

	protected void doStop(ServiceContext serviceContext) {

		//TODO this can be paralelized ?
		for (ModularApplicationRegistry registry : this.registar.values()){

			registry.stop(serviceContext);

		}

	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public ApplicationRegistry registry(String applicationId, Version version,
			ApplicationActivator activator) {

		if (registar.containsKey(applicationId)){
			throw new ApplicationAlreadyRegistredException(applicationId);
		}

		ModularApplicationRegistry registry = new ModularApplicationRegistry(applicationId, version, activator);

		registar.put(applicationId, registry);

		return registry;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ApplicationRegistry getRegistry(String applicationId) {
		return registar.get(applicationId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<ApplicationRegistry> getAll() {
		return Collections.<ApplicationRegistry>unmodifiableCollection(registar.values());
	}


	
	/**
	 * @param serviceContext
	 */
	public void unloadApplications() {
		throw new UnsupportedOperationException("Not implememented yet");
	}






}
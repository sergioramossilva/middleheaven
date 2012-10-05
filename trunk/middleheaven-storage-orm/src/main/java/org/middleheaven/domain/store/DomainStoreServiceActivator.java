package org.middleheaven.domain.store;

import java.util.Collection;

import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.core.wiring.WiringService;

public class DomainStoreServiceActivator extends ServiceActivator {

	private HashDomainStoreService storeService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(new ServiceSpecification(WiringService.class));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(new ServiceSpecification(DomainStoreService.class));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ServiceContext serviceContext) {
		storeService = new HashDomainStoreService();

		// install an EntityStore provider
		
		WiringService wiringService = serviceContext.getService(WiringService.class);
				
		
//		wiringService.addConfiguration( new BindConfiguration(){
//
//			@Override
//			public void configure(Binder binder) {
//				Class cr = DomainStoreResolver.class;
//				binder.bind(DomainStoreResolver.class).in(Shared.class).toResolver(cr);
//			}
//
//		});
		
		serviceContext.register(DomainStoreService.class, storeService);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
		serviceContext.unRegister(DomainStoreService.class);
	}
}

/**
 * 
 */
package org.middleheaven.domain.store;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.Resolver;
import org.middleheaven.core.wiring.WiringSpecification;

/**
 * Resolver {@link DomainStore}s for wiring.
 */
class DomainStoreResolver implements Resolver<DomainStore> {

	
	public DomainStoreResolver(){}
	
	@Override
	public DomainStore resolve(WiringSpecification<DomainStore> specification) {
		
		final Object name = specification.getParam("name");
		return new NamedEntityStore(name == null ? null : name.toString());
	}
	
	
	public static class NamedEntityStore extends AbstractEntityStorageDecorator{
		
		private String name;
		private DomainStoreService service;
		
		public NamedEntityStore(String name){
			this.name = name;
			service = ServiceRegistry.getService(DomainStoreService.class);
		}
		
		@Override
		protected DomainStore original() {
			return (name==null) ? service.getStore() : service.getStore(name);	
		}
		
		
	}
}
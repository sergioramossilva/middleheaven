/**
 * 
 */
package org.middleheaven.domain.store;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.ResolutionContext;
import org.middleheaven.core.wiring.Resolver;
import org.middleheaven.core.wiring.WiringQuery;

/**
 * Resolver {@link DomainStore}s for wiring.
 */
class DomainStoreResolver implements Resolver {

	
	public DomainStoreResolver(){}
	
	@Override
	public Object resolve(ResolutionContext context, WiringQuery query) {
		
		final Object name = query.getParam("name");
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
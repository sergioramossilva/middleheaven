/**
 * 
 */
package org.middleheaven.storage;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.Resolver;
import org.middleheaven.core.wiring.WiringSpecification;

class EntityStoreResolver implements Resolver<EntityStore> {

	
	public EntityStoreResolver(){}
	
	@Override
	public EntityStore resolve(WiringSpecification specification) {
		
		return new NamedEntityStore(specification.getParam("name"));
	}
	
	
	public static class NamedEntityStore extends AbstractEntityStorageDecorator{
		
		private String name;
		private EntityStoreService service;
		
		public NamedEntityStore(String name){
			this.name = name;
			service = ServiceRegistry.getService(EntityStoreService.class);
		}
		
		@Override
		protected EntityStore original() {
			return (name==null) ? service.getStore() : service.getStore(name);	
		}
		
		
	}
}
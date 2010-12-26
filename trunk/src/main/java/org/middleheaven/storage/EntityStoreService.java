package org.middleheaven.storage;

import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.model.domain.DomainModel;

@Service
public interface EntityStoreService {

	
	public void registerStore (String name, DataStorage dataStorage, DomainModel domainModel);
	public void unRegisterStore (String name);
	public void unRegisterAll();
	
	/**
	 * Return the first registered EntityStore instance allocated to the current transaction. 
	 * @return the first registered EntityStore.  
	 */
	public EntityStore getStore();
	
	public EntityStore getStore(String name);
}

package org.middleheaven.storage;

import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.domain.DomainModel;

@Service
public interface EntityStoreService {

	
	public void register (String name, DataStorage dataStorage, DomainModel domainModel);
	public void unRegister (String name);
	public void unRegisterAll();
	
	/**
	 * Return the first registered EntityStore. 
	 * @return the first registered EntityStore.  
	 */
	public EntityStore getStore();
	
	public EntityStore getStore(String name);
}

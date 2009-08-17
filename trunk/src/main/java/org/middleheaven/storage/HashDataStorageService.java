/**
 * Data: 21/10/2008
 *
 * Copyright 2008, BRISA
 * 
 */
package org.middleheaven.storage;

import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.core.wiring.service.Service;

@Service
public class HashDataStorageService implements DataStorageService{

	private Map<String, EntityStore> storages = new TreeMap<String, EntityStore>();
	
	@Override
	public void addDataStorage(String storageID, EntityStore storage) {
		storages.put(storageID,storage);
	}

	@Override
	public void removeDataStorage(String storageID) {
		storages.remove(storageID);
	}
	
	@Override
	public EntityStore getStorage(String storageID) {
		return storages.get(storageID);
	}

	@Override
	public EntityStore getStorage() {
		if (storages.isEmpty()){
			throw new DataStorageNotFoundException();
		}
		return storages.values().iterator().next();
	}


	
}
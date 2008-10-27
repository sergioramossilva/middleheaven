/**
 * Data: 21/10/2008
 *
 * Copyright 2008, BRISA
 * 
 */
package org.middleheaven.storage;

import java.util.Map;
import java.util.TreeMap;

public class HashDataStorageService implements DataStorageService{

	private Map<String, DataStorage> storages = new TreeMap<String, DataStorage>();
	
	@Override
	public void addDataStorage(String storageID, DataStorage storage) {
		storages.put(storageID,storage);
	}

	@Override
	public void removeDataStorage(String storageID) {
		storages.remove(storageID);
	}
	
	@Override
	public DataStorage getStorage(String storageID) {
		return storages.get(storageID);
	}

	@Override
	public DataStorage getStorage() {
		if (storages.isEmpty()){
			throw new DataStorageNotFoundException();
		}
		return storages.values().iterator().next();
	}


	
}
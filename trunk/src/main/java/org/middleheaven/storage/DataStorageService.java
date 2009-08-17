package org.middleheaven.storage;

public interface DataStorageService {

	
	public EntityStore getStorage(String storageID);
	public EntityStore getStorage();
	
	public void addDataStorage(String storageID , EntityStore storage);
	public void removeDataStorage(String storageID);
}

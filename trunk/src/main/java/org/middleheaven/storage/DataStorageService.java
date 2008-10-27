package org.middleheaven.storage;

public interface DataStorageService {

	
	public DataStorage getStorage(String storageID);
	public DataStorage getStorage();
	
	public void addDataStorage(String storageID , DataStorage storage);
	public void removeDataStorage(String storageID);
}

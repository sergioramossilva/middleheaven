package org.middleheaven.persistance;

import org.middleheaven.core.annotations.Service;

/**
 * Provides access to {@link DataStore}s and {@link DataStoreSchema}s.
 */
@Service
public interface DataService {

	
	public void addProvider(DataStoreProvider provider);
	public void removeProvider(DataStoreProvider provider);
	
	
	public DataStore getDataStore (DataStoreName name) throws DataStoreNotFoundException;

	/**
	 * Shortcut method to search for a {@link DataStoreSchema}.
	 * 
	 * @param name
	 * @return
	 * @throws DataStoreSchemaNotFoundException
	 */
	public DataStoreSchema getDataStoreSchema(DataStoreSchemaName name) throws DataStoreSchemaNotFoundException;

}

/**
 * 
 */
package org.middleheaven.persistance;

import java.util.LinkedList;
import java.util.List;

import org.middleheaven.util.identity.IdentitySequence;
import org.middleheaven.util.identity.IntegerIdentity;

/**
 * 
 */
class DefaultDataService implements DataService {

	
	private final List<DataStoreProvider>  providers = new LinkedList<DataStoreProvider>();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addProvider(DataStoreProvider provider) {
		providers.add(provider);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeProvider(DataStoreProvider provider) {
		providers.remove(provider);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataStore getDataStore(DataStoreName name) throws DataStoreNotFoundException {
		for (DataStoreProvider p : providers){
			if (p.isProviderDataStore(name)){
				return p.getDataStore(name);
			}
		}
		
		throw new DataStoreNotFoundException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataStoreSchema getDataStoreSchema(DataStoreSchemaName name) throws DataStoreSchemaNotFoundException {
		try {
			for (DataStoreProvider p : providers){
				if (p.isProviderDataStore(name.getDataStoreName())){
					return p.getDataStore(name.getDataStoreName()).getDataStoreSchema(name);
				}
			}
			
			throw new DataStoreSchemaNotFoundException();
			
		} catch (DataStoreNotFoundException e){
			throw new DataStoreSchemaNotFoundException(e);
			
		}
		
		
	}

	/**
	 * 
	 */
	public void close() {
		// no-op
	}








}

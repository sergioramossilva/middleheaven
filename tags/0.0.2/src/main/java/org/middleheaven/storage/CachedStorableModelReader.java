package org.middleheaven.storage;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.validation.Consistencies;

public class CachedStorableModelReader implements StorableModelReader {

	
	private final Map<String, StorableEntityModel> entityModels = new HashMap<String, StorableEntityModel>();
	private StorableModelReader reader;
	
	public CachedStorableModelReader(StorableModelReader reader){
		this.reader = reader;
	}
	
	/**
	 * @see org.middleheaven.storage.StorableModelResolver#resolveModel(java.lang.Class)
	 */
	public StorableEntityModel read(Class<?> entityType) {
		Consistencies.consistNotNull(entityType);
		
		StorableEntityModel sm = entityModels.get(entityType.getName());
		if (sm==null){
			sm = reader.read(entityType);
			entityModels.put(entityType.getName(),sm);
		}
		return sm;
		
	}
	

	
}

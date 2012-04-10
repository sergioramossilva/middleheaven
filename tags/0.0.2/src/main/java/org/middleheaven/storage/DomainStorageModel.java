package org.middleheaven.storage;

import java.util.Collection;

public interface DomainStorageModel {


	public EntityStore storageOf( String entityName);
	public StorableEntityModel getStorableEntityModelFor(String entityName);
	public Collection<StorableEntityModel> storableEntitiesModels();
}
package org.middleheaven.storage;

public interface StoreMetadataManager {

	StorableEntityModel getStorageModel(Class<?> type);
}

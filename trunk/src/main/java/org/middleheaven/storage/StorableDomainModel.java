package org.middleheaven.storage;

import org.middleheaven.util.identity.Identity;

public interface StorableDomainModel {

	StorableEntityModel getStorageModel(Class<?> type);

	Class<? extends Identity> indentityTypeFor(Class<?> entityType);
}

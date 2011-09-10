package org.middleheaven.storage;

import org.middleheaven.util.criteria.entity.EntityCriteria;

public interface ExecutableQuery<T> extends Query<T>{

	public EntityCriteria<T> getCriteria();

	public StorableEntityModel getModel();
}
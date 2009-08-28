package org.middleheaven.storage;

import org.middleheaven.storage.criteria.Criteria;

public interface ExecutableQuery<T> extends Query<T>{

	public Criteria<T> getCriteria();

	public StorableEntityModel getModel();
}
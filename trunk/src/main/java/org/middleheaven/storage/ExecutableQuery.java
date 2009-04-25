package org.middleheaven.storage;

import org.middleheaven.storage.criteria.Criteria;

public interface ExecutableQuery<T> extends Query<T>{

	public abstract Criteria<T> getCriteria();

	public abstract StorableEntityModel getModel();


}
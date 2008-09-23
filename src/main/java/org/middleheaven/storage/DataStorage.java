package org.middleheaven.storage;

import org.middleheaven.storage.criteria.Criteria;

public interface DataStorage {

	
	public <T> T store(T obj);
	public <T> void remove(T obj);
	public <T> Query<T> createQuery(Criteria<T> criteria);
	public <T> Query<T> createQuery(Criteria<T> criteria, ReadStrategy strategy);
	public <T> void remove(Criteria<T> criteria);
}

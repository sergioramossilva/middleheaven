package org.middleheaven.storage.inmemory;

import java.util.Collection;

import org.middleheaven.storage.Query;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.criteria.Criteria;

public class InMemoryCriteriaQuery<T> implements Query<T> {

	Criteria<T> criteria;
	InMemoryStoreKeeper manager;
	StorableEntityModel model;
	
	public InMemoryCriteriaQuery(Criteria<T> criteria, StorableEntityModel model, InMemoryStoreKeeper manager){
		this.criteria = criteria;
		this.manager = manager;
		this.model = model;
	}
	
	@Override
	public long count() {
		return list().size();
	}

	@Override
	public T find() {
		return list().iterator().next();
	}

	@Override
	public Collection<T> list() {
		return manager.execute(criteria,model);
	}

	@Override
	public boolean isEmpty() {
		return list().isEmpty();
	}

}

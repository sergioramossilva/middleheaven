package org.middleheaven.domain.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.storage.ListQuery;
import org.middleheaven.storage.Query;
import org.middleheaven.util.identity.Identity;


public abstract class AbstractHashRepository<E> extends AbstractRepository<E> {

	private Map<Identity, E> instances = new HashMap<Identity, E>();
	
	@Override
	public Query<E> findAll() {
		return new ListQuery<E>(new ArrayList<E>(this.instances.values()));
	}

	@Override
	public Query<E> findByIdentity(Identity id) {
		return new ListQuery<E>(Collections.singletonList(instances.get(id)));
	}

	@Override
	public void remove(E instance) {
		instances.remove(this.getIdentityFor(instance));
	}

	protected void put(Identity id , E instance){
		instances.put(id, instance);
	}
}

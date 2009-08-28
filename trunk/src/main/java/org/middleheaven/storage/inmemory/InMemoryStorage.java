package org.middleheaven.storage.inmemory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.middleheaven.sequence.Sequence;
import org.middleheaven.storage.AbstractSequencialIdentityStorage;
import org.middleheaven.storage.ExecutableQuery;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.QueryExecuter;
import org.middleheaven.storage.ReadStrategy;
import org.middleheaven.storage.SimpleExecutableQuery;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaFilter;
import org.middleheaven.storage.db.StoreQuerySession;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IntegerIdentitySequence;

public class InMemoryStorage extends AbstractSequencialIdentityStorage {

	public InMemoryStorage() {
		super(null);
	}

	// Map<entity_name , Map<identity, Storable>
	final Map<String, Map<Identity,Storable> > data = new HashMap<String,Map<Identity,Storable> >();
	final Map<String, Sequence<Identity>> sequences = new HashMap<String,Sequence<Identity>>();

	Collection<Storable> getBulkData(String name){
		Map<Identity,Storable> col = data.get(name);
		if (col==null){
			return Collections.emptySet();
		}
		return col.values();
	}


	private <T> void setBulkData(Collection<Storable> collection){
		if (!collection.isEmpty()){
			Storable s = collection.iterator().next();
			Map<Identity,Storable> col = data.get(s.getPersistableClass().getName());
			if (col==null){
				col = new HashMap<Identity,Storable>();
			}
			for (Storable ss: collection){
				col.put(ss.getIdentity(), ss);
			}
			data.put(s.getPersistableClass().getName(),col);
		}

	}

	private QueryExecuter queryExecuter = new QueryExecuter() {

		@Override
		public <T> Collection<T> execute(final ExecutableQuery<T> query) {
			Criteria<T> criteria = query.getCriteria();
			Collection<Storable> all = getBulkData(criteria.getTargetClass().getName());
			if (all.isEmpty()){
				return Collections.emptySet();
			}

			CriteriaFilter<T> filter = new CriteriaFilter<T>(criteria,query.getModel());
			Collection<T> result = new LinkedList<T>();
			for (Storable s : all){
				T obj = criteria.getTargetClass().cast(s);
				if(filter.classify(obj)){
					result.add(obj);
				}
			}

			return result;

		}

	};

	@Override
	public <T> Query<T> createQuery(Criteria<T> criteria, ReadStrategy strategy) {
		return new SimpleExecutableQuery<T>(criteria, null, queryExecuter);
	}

	@Override
	public <I extends Identity> Sequence<I>  getSequence(String name) {
		Sequence<I> seq= (Sequence<I>) sequences.get(name);
		if (seq ==null){
			seq = (Sequence<I>) new IntegerIdentitySequence();
		}
		return seq;
	}

	@Override
	public void insert(Collection<Storable> collection) {
		this.setBulkData(collection);


	}


	@Override
	public void update(Collection<Storable> collection) {
		if (!collection.isEmpty()){
			Storable s = collection.iterator().next();
			this.getBulkData(s.getPersistableClass().getName()).removeAll(collection);
			this.setBulkData(collection);
		}
	}

	@SuppressWarnings("unchecked") // all objects in collection are Storable
	@Override
	public void remove(Criteria<?> criteria) {
		Query all = this.createQuery(criteria, null);

		this.remove(all.findAll());
	}

	@Override
	public void remove(Collection<Storable> collection) {
		if (!collection.isEmpty()){
			Storable s = collection.iterator().next();
			this.getBulkData(s.getPersistableClass().getName()).removeAll(collection);
		}
	}





}

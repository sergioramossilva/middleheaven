package org.middleheaven.storage.inmemory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.middleheaven.storage.AbstractStoreManager;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.ReadStrategy;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.util.sequence.LongSequence;
import org.middleheaven.util.sequence.Sequence;

public class NaiveStoreManager extends AbstractStoreManager {

	final Map<String, Collection<Storable> > data = new HashMap<String, Collection<Storable> >();
	final Map<String, Sequence<Long>> sequences = new HashMap<String,Sequence<Long>>();

    Collection<Storable> getBulkData(String name){
		Collection<Storable> col = data.get(name);
		if (col==null){
			return Collections.emptySet();
		}
		return col;
	}

	private <T> void setBulkData(Collection<Storable> collection){
		if (!collection.isEmpty()){
			Storable s = collection.iterator().next();
			Collection<Storable> col = data.get(s.getPersistableClass().getName());
			if (col==null){
				col = new LinkedList<Storable>();
			}
			col.addAll(collection);
			data.put(s.getPersistableClass().getName(),col);
		}
		
	}

	@Override
	public <T> Query<T> createQuery(Criteria<T> criteria,StorableEntityModel model, ReadStrategy strategy) {
		return new NaiveCriteriaQuery<T>(criteria, this);
	}

	@Override
	public Sequence<Long> getSequence(String name) {
		Sequence<Long> seq= sequences.get(name);
		if (seq ==null){
			seq = new LongSequence();
		}
		return seq;
	}

	@Override
	public void insert(Collection<Storable> collection,StorableEntityModel model) {
		this.setBulkData(collection);
		

	}

	@Override
	public void remove(Collection<Storable> collection,StorableEntityModel model) {
		if (!collection.isEmpty()){
			Storable s = collection.iterator().next();
			this.getBulkData(s.getPersistableClass().getName()).removeAll(collection);
		}
	}

	@Override
	public void update(Collection<Storable> collection,StorableEntityModel model) {
		if (!collection.isEmpty()){
			Storable s = collection.iterator().next();
			this.getBulkData(s.getPersistableClass().getName()).removeAll(collection);
			this.setBulkData(collection);
			
		}
	}

	@Override
	public void remove(Criteria<?> criteria, StorableEntityModel model) {
		// TODO Auto-generated method stub
		
	}



}

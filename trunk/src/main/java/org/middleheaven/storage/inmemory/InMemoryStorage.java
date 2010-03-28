package org.middleheaven.storage.inmemory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.middleheaven.sequence.Sequence;
import org.middleheaven.storage.AbstractSequencialIdentityStorage;
import org.middleheaven.storage.ExecutableQuery;
import org.middleheaven.storage.HashStorable;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.QueryExecuter;
import org.middleheaven.storage.ReadStrategy;
import org.middleheaven.storage.SimpleExecutableQuery;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaFilter;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.collections.TransformedCollection;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IntegerIdentitySequence;

/**
 * Stores the storable objects in memory as copies of the real objects.
 * TODO disconnect instances in relation 1-1, 1-n , n-1 , n-m 
 */
public class InMemoryStorage extends AbstractSequencialIdentityStorage {

	private final Map<String, Map<Identity,HashStorable> > data = new HashMap<String,Map<Identity,HashStorable> >();
	private final Map<String, Sequence<? extends Identity>> sequences = new HashMap<String,Sequence<? extends Identity>>();

	
	public InMemoryStorage() {
		super(null);
	}

	private Collection<Storable> getBulkData(String name){
		Map<Identity,HashStorable> col = data.get(name);
		if (col==null){
			return Collections.emptySet();
		}
		return TransformedCollection.transform( col.values() , new Classifier<Storable,HashStorable>(){

			@Override
			public Storable classify(HashStorable obj) {
				if (obj == null){
					return null;
				}
				
				Storable s = getStorableStateManager().merge(obj.getEntityModel().newInstance());
				
				obj.copyTo(s);
				
				return s;
			}
			
		});
	}


	private <T> void setBulkData(Collection<Storable> collection){
		if (!collection.isEmpty()){
			Storable s = collection.iterator().next();
			Map<Identity,HashStorable> col = data.get(s.getPersistableClass().getName());
			if (col==null){
				col = new HashMap<Identity,HashStorable>();
			}
			for (Storable ss: collection){
				col.put(ss.getIdentity(), HashStorable.clone(ss));
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
	public <I extends Identity> Sequence<I>  getSequence(Class<?> entityType) {
		Sequence<I> seq= (Sequence<I>) sequences.get(entityType.getName());
		if (seq ==null){
			seq = (Sequence<I>) new IntegerIdentitySequence();
			sequences.put(entityType.getName(), seq);
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

		this.remove(all.all());
	}

	@Override
	public void remove(Collection<Storable> collection) {
		if (!collection.isEmpty()){
			Storable s = collection.iterator().next();
			this.getBulkData(s.getPersistableClass().getName()).removeAll(collection);
		}
	}

}

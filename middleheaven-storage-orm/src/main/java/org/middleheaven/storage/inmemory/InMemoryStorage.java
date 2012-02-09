//package org.middleheaven.storage.inmemory;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.Map;
//
//import org.middleheaven.domain.query.ExecutableQuery;
//import org.middleheaven.domain.query.Query;
//import org.middleheaven.domain.query.QueryExecuter;
//import org.middleheaven.domain.store.EntityInstance;
//import org.middleheaven.domain.store.MetaBeanEntityInstance;
//import org.middleheaven.domain.store.ReadStrategy;
//import org.middleheaven.sequence.Sequence;
//import org.middleheaven.storage.SimpleExecutableQuery;
//import org.middleheaven.storage.storable.AbstractSequencialIdentityStorage;
//import org.middleheaven.util.classification.Classifier;
//import org.middleheaven.util.collections.TransformedCollection;
//import org.middleheaven.util.criteria.entity.CriteriaFilter;
//import org.middleheaven.util.criteria.entity.EntityCriteria;
//import org.middleheaven.util.identity.Identity;
//import org.middleheaven.util.identity.IntegerIdentitySequence;
//
///**
// * Stores the storable objects in memory as copies of the real objects.
// * TODO disconnect instances in relation 1-1, 1-n , n-1 , n-m 
// */
//public class InMemoryStorage extends AbstractSequencialIdentityStorage {
//
//	private final Map<String, Map<Identity,MetaBeanEntityInstance> > data = new HashMap<String,Map<Identity,MetaBeanEntityInstance> >();
//	private final Map<String, Sequence<? extends Identity>> sequences = new HashMap<String,Sequence<? extends Identity>>();
//
//	
//	public InMemoryStorage() {
//		super(null);
//	}
//
//	private Collection<EntityInstance> getBulkData(String name){
//		Map<Identity,MetaBeanEntityInstance> col = data.get(name);
//		if (col==null){
//			return Collections.emptySet();
//		}
//	
//		return TransformedCollection.transform( col.values() , new Classifier<EntityInstance,MetaBeanEntityInstance>(){
//
//			@Override
//			public EntityInstance classify(MetaBeanEntityInstance obj) {
//				if (obj == null){
//					return null;
//				}
//				
//				EntityInstance s = getStorableStateManager().merge(newInstance(obj.getEntityModel().getEntityClass()));
//				
//				obj.copyTo(s);
//				
//				return s;
//			}
//			
//		});
//	}
//
//
//	private <T> void setBulkData(Collection<EntityInstance> collection){
//		if (!collection.isEmpty()){
//			EntityInstance s = collection.iterator().next();
//			Map<Identity,MetaBeanEntityInstance> col = data.get(s.getPersistableClass().getSimpleName());
//			if (col==null){
//				col = new HashMap<Identity,MetaBeanEntityInstance>();
//			}
//			for (EntityInstance ss: collection){
//				col.put(ss.getIdentity(), MetaBeanEntityInstance.clone(ss));
//			}
//			data.put(s.getPersistableClass().getSimpleName(),col);
//		}
//
//	}
//
//	private QueryExecuter queryExecuter = new QueryExecuter() {
//
//		@Override
//		public <T> Collection<T> execute(final ExecutableQuery<T> query) {
//			EntityCriteria<T> criteria = query.getCriteria();
//			Collection<EntityInstance> all = getBulkData(criteria.getTargetClass().getName());
//			if (all.isEmpty()){
//				return Collections.emptySet();
//			}
//
//			CriteriaFilter<T> filter = new CriteriaFilter<T>(criteria,query.getModel(),InMemoryStorage.this);
//			Collection<T> result = new LinkedList<T>();
//			for (EntityInstance s : all){
//				T obj = criteria.getTargetClass().cast(s);
//				if(filter.classify(obj)){
//					result.add(obj);
//				}
//			}
//
//			return result;
//
//		}
//
//	};
//
//	@Override
//	public <T> Query<T> createQuery(EntityCriteria<T> criteria, ReadStrategy strategy) {
//		return new SimpleExecutableQuery<T>(criteria, null, queryExecuter);
//	}
//
//	@Override
//	public <I extends Identity> Sequence<I>  getSequence(Class<?> entityType) {
//		Sequence<I> seq= (Sequence<I>) sequences.get(entityType.getName());
//		if (seq ==null){
//			seq = (Sequence<I>) new IntegerIdentitySequence();
//			sequences.put(entityType.getName(), seq);
//		}
//		return seq;
//	}
//
//	@Override
//	public void insert(Collection<EntityInstance> collection) {
//		this.setBulkData(collection);
//	}
//
//
//	@Override
//	public void update(Collection<EntityInstance> collection) {
//		if (!collection.isEmpty()){
//			EntityInstance s = collection.iterator().next();
//			this.getBulkData(s.getPersistableClass().getSimpleName()).removeAll(collection);
//			this.setBulkData(collection);
//		}
//	}
//
//	@SuppressWarnings("unchecked") // all objects in collection are Storable
//	@Override
//	public void remove(EntityCriteria<?> criteria) {
//		Query all = this.createQuery(criteria, null);
//
//		this.remove(all.fetchAll());
//	}
//
//	@Override
//	public void remove(Collection<EntityInstance> collection) {
//		if (!collection.isEmpty()){
//			EntityInstance s = collection.iterator().next();
//			this.getBulkData(s.getPersistableClass().getSimpleName()).removeAll(collection);
//		}
//	}
//
//
//
//}

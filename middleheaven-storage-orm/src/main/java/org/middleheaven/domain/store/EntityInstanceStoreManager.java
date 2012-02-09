package org.middleheaven.domain.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.middleheaven.core.reflection.metaclass.MetaBean;
import org.middleheaven.core.reflection.metaclass.ReflectionBean;
import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.domain.model.DomainModel;
import org.middleheaven.domain.model.EntityFieldModel;
import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.domain.query.Query;
import org.middleheaven.domain.query.QueryExecuter;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.Walker;
import org.middleheaven.util.identity.Identity;


/**
 * Common storable state controller.
 */
final class EntityInstanceStoreManager implements DomainStoreManager {

	private final EntityInstanceStorage storage;
	private final DomainModel domainModel;
	private final IdentityManager identityManager;

	public EntityInstanceStoreManager (IdentityManager identityManager, EntityInstanceStorage storage, DomainModel domainModel){
		this.identityManager = identityManager;
		this.storage = storage;
		this.storage.setStorableStateManager(this);
		this.domainModel = domainModel;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> Query<T> createQuery(final EntityCriteria<T> criteria, final ReadStrategy strategy, final StorageUnit unit) {
		
		
		return new LayzEntityCriteriaQuery<T>(criteria, new QueryExecuter (){

			@Override
			public <E> Collection<E> execute(EntityCriteria<E> c) {
				
				Collection all = storage.createQuery(c , strategy).fetchAll();
				
				all = unit.filter(all, c.getTargetClass());
				
				return all;
			}
			
		});

	}
	
	/**
	 * Wraps the object with a {@link EntityInstance} interface.
	 * If the object is already a {@link EntityInstance}, return the object as it is.
	 * 
	 * @param obj the object to merge.
	 * @return
	 */
	public EntityInstance merge(Object obj){
		EntityInstance p;
		if (obj instanceof EntityInstance){
			p = (EntityInstance) obj;
		} else {

			// not managed yet

			if (obj instanceof MetaBean){
				EntityModel model = domainModel.getModelFor(((MetaBean) obj).getMetaClass().getName());
				p = new MetaBeanEntityInstance(model, (MetaBean) obj);
			} else {
				EntityModel model = domainModel.getModelFor(obj.getClass().getName());
				p = new MetaBeanEntityInstance(model, new ReflectionBean(obj));
			}

		}
		return p;
	}
	
	
	public EntityInstance newEntityInstance(EntityModel model){
		return merge(model.getEntityClass().newInstance());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Identity getIdentityFor(Object object) {
		if (object instanceof EntityInstance) {
			return ((EntityInstance) object).getIdentity();
		}
		return null;
	}
	
	private StoreAction assignAction(EntityInstance p){
		switch (p.getStorableState()) {
		case DELETED:
			if (p.getIdentity() == null) {
				return null;
			}
			return new DeleteAction(p);
		case FILLED:
		case EDITED:
		case NOT_PERSISTED:
			if (p.getIdentity() == null) {
				identityManager.assignIdentity(p);
				return new InsertAction(p);
			} else {
				// update
				return new UpdateAction(p);
			}
		case RETRIVED:
			// no-op
			return null;
		default:
			throw new IllegalStateException(p.getStorableState() + " is unkown");
		}
	}
	 
	public void flatten(EntityInstance p, Set<EntityInstance> all){

		if (all.contains(p)){ // loop
			return;
		}

		// add only if it will cause any change
		if (!p.getStorableState().isNeutral()){
			all.add(p);
		}

		for (EntityInstanceField field : p.getFields()) {
			EntityFieldModel fieldModel = field.getModel();
			if(fieldModel.getDataType().isToOneReference()){
				EntityInstance merged = this.merge(field.getValue());
				if(merged != null){
					field.setValue(merged);
					flatten(merged, all);
				}
			} else if (fieldModel.getDataType().isToManyReference()){
			
				@SuppressWarnings({ "unchecked", "rawtypes" })
				Collection<?> allRefereed = new ArrayList((Collection<?>) field.getValue());
				for (Object o : allRefereed){
					if(o != null){
						EntityInstance merged = this.merge(o);
						field.remove(o);
						field.add(merged);
						flatten(merged, all);
					}
				}
			}
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> void remove(T obj, StorageUnit unit) {
		EntityInstance p = this.merge(obj);
		if (p.getIdentity()!=null) {
			p.setStorableState(StorableState.DELETED);
			unit.addAction(assignAction(p));
		}
		// else
		// not identified
		// do nothing as this object is not in the store

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T store(T obj, final StorageUnit unit) {

		EntityInstance p = this.merge(obj);
		final Set<EntityInstance> all = new HashSet<EntityInstance>();
		
		
	
		flatten(p,all);


		CollectionUtils.enhance(all).collect(new Classifier<StoreAction, EntityInstance>(){

			@Override
			public StoreAction classify(EntityInstance s) {
				return assignAction(s);
			}

		}).each(new Walker<StoreAction>(){

			@Override
			public void doWith(StoreAction action) {
				unit.addAction(action);
			}

		});

		return (T) p;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit(StorageUnit unit) {
		unit.commitTo(storage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void roolback(StorageUnit unit) {
		unit.roolback();
	}

	@Override
	public DomainModel getDomainModel() {
		return domainModel;
	}
}

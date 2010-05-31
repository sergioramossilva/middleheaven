package org.middleheaven.domain.repository;

import org.middleheaven.storage.DataStorageListener;
import org.middleheaven.storage.EntityStore;
import org.middleheaven.storage.EntityStoreService;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.StorageChangeEvent;
import org.middleheaven.util.criteria.entity.EntityCriteriaBuilder;
import org.middleheaven.util.identity.Identity;


public class EntityStoreRepository<E> extends AbstractRepository<E>  {

	private Class<E> entityType;
	private DataStorageListener storageListener;
	private EntityStoreService entityStoreService;

	public EntityStoreRepository(EntityStoreService entityStoreService){
		this.entityType = this.getEntityClass();
		this.entityStoreService = entityStoreService;
	}

	protected EntityStore getEntityStore(){
		return entityStoreService.getStore();
	}

	
	@Override
	public void addRepositoryListener(RepositoryListener listener) {
		super.addRepositoryListener(listener);

		if (storageListener==null){
			storageListener = new DataStorageListener(){

				@Override
				public void onStorageChange(StorageChangeEvent event) {
					if (entityType.isInstance(event.getInstance())){
						fireChangeEvent(entityType.cast(event.getInstance()),
								event.isRemoved(),
								event.isAdded(),
								event.isUpdated()
						);
					}
				}

			};
			getEntityStore().addStorageListener(storageListener);
		}

	}


	@Override
	public void remove(E intance) {
		getEntityStore().remove(intance);
	}

	@Override
	public E store(E entity) {
		return getEntityStore().store(entity);
	}

	@Override
	public Query<E> findAll() {
		return getEntityStore().createQuery(EntityCriteriaBuilder.search(entityType).all());
	}

	@Override
	public Query<E> findEquals(E instance) {
		return getEntityStore().createQuery(EntityCriteriaBuilder.search(entityType).isEqual(instance).all());
	}

	@Override
	public Query<E> findIdentical(E instance) {
		return getEntityStore().createQuery(EntityCriteriaBuilder.search(entityType).isIdentical(instance).all());
	}

	@Override
	public Query<E> findByIdentity(Identity id) {
		return getEntityStore().createQuery(EntityCriteriaBuilder.search(entityType)
				.hasIdentity(id)
				.all()
		);
	}

	@Override
	public Identity getIdentityFor(E instance) {
		return this.getEntityStore().getIdentityFor(instance);
	}





}

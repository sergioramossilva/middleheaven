package org.middleheaven.domain.repository;

import org.middleheaven.domain.criteria.EntityCriteriaBuilder;
import org.middleheaven.domain.query.Query;
import org.middleheaven.domain.store.DomainChangeEvent;
import org.middleheaven.domain.store.DomainStore;
import org.middleheaven.domain.store.DomainStoreListener;
import org.middleheaven.domain.store.DomainStoreService;
import org.middleheaven.util.identity.Identity;


public class EntityStoreRepository<E> extends AbstractRepository<E>  {

	private Class<E> entityType;
	private DomainStoreListener storageListener;
	private DomainStoreService entityStoreService;

	public EntityStoreRepository(DomainStoreService entityStoreService){
		this.entityType = this.getEntityClass();
		this.entityStoreService = entityStoreService;
	}

	protected DomainStore getEntityStore(){
		return entityStoreService.getStore();
	}

	
	@Override
	public void addRepositoryListener(RepositoryListener listener) {
		super.addRepositoryListener(listener);

		if (storageListener==null){
			storageListener = new DomainStoreListener(){

				@Override
				public void onEntityAdded(DomainChangeEvent event) {
					if (entityType.isInstance(event.getInstance())){
						fireChangeEvent(entityType.cast(event.getInstance()),
								false,
								true,
								false
						);
					}
				}

				@Override
				public void onEntityRemoved(DomainChangeEvent event) {
					if (entityType.isInstance(event.getInstance())){
						fireChangeEvent(entityType.cast(event.getInstance()),
								true,
								false,
								false
						);
					}
				}

				@Override
				public void onEntityChanged(DomainChangeEvent event) {
					if (entityType.isInstance(event.getInstance())){
						fireChangeEvent(entityType.cast(event.getInstance()),
								false,
								false,
								true
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

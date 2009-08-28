package org.middleheaven.domain.repository;

import org.middleheaven.domain.DomainModel;
import org.middleheaven.storage.EntityStore;
import org.middleheaven.storage.DataStorageListener;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.StorageChangeEvent;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.util.identity.Identity;


public class DomainStorageRepository<E> extends AbstractRepository<E>  {

	private Class<E> entityType;
	private DomainModel domainModel;
	private DataStorageListener storageListener;
	private EntityStore dataStorage;

	public DomainStorageRepository(Class<E> entityType,EntityStore dataStorage){
		this.entityType = entityType;
		this.dataStorage = dataStorage;
	}

	public EntityStore getDataStorage(){
		return dataStorage;
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
			getDataStorage().addStorageListener(storageListener);
		}

	}

	@Override
	public void setDomainModel(DomainModel domainModel) {
		this.domainModel = domainModel;
	}


	@Override
	public void remove(E intance) {
		getDataStorage().remove(intance);
	}

	@Override
	public E store(E entity) {
		return getDataStorage().store(entity);
	}

	@Override
	public Query<E> findAll() {
		return getDataStorage().createQuery(CriteriaBuilder.search(entityType).all());
	}

	@Override
	public Query<E> findEquals(E instance) {
		return getDataStorage().createQuery(CriteriaBuilder.search(entityType).isEqual(instance).all());
	}

	@Override
	public Query<E> findIdentical(E instance) {
		return getDataStorage().createQuery(CriteriaBuilder.search(entityType).isIdentical(instance).all());
	}

	@Override
	public Query<E> findByIdentity(Identity id) {
		return getDataStorage().createQuery(CriteriaBuilder.search(entityType)
				.and(this.domainModel.getEntityModelFor(entityType).identityFieldModel().getLogicName().getName()).eq(id)
				.all()
		);
	}

	@Override
	public Identity getIdentityFor(E instance) {
		return dataStorage.getIdentityFor(instance);
	}





}

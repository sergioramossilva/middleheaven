package org.middleheaven.domain.repository;

import org.middleheaven.domain.DomainModel;
import org.middleheaven.storage.DataStorage;
import org.middleheaven.storage.DataStorageListener;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.StorageChangeEvent;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.util.identity.Identity;


public class StandardEntityRepository<E> extends AbstractRepository<E>  {

	private Class<E> entityType;
	private DomainModel domainModel;
	private DataStorageListener storageListener;

	public StandardEntityRepository(Class<E> entityType){
		this.entityType = entityType;

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

	protected DataStorage getDataStorage(){
		return domainModel.storageOf(entityType);
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
	public Query<E> findSame(E instance) {
		return getDataStorage().createQuery(CriteriaBuilder.search(entityType).isSame(instance).all());
	}

	@Override
	public Query<E> findByIdentity(Identity id) {
		return getDataStorage().createQuery(CriteriaBuilder.search(entityType)
				.and(this.domainModel.getEntityModelFor(entityType).identityFieldModel().getLogicName().getColumnName()).eq(id)
				.all()
		);
	}





}

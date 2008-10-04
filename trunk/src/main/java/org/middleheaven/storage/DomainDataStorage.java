package org.middleheaven.storage;

import java.util.Collections;

import org.middleheaven.core.reflection.ProxyUtils;
import org.middleheaven.storage.criteria.Criteria;

public class DomainDataStorage implements DataStorage {

	StoreKeeper storeManager;
	StoreMetadataManager metadataService;
	
	public DomainDataStorage(StoreKeeper storeManager,StoreMetadataManager metadataService) {
		this.storeManager = storeManager;
		this.metadataService = metadataService;
	}

	@Override
	public <T> Query<T> createQuery(Criteria<T> criteria) {
		return this.createQuery(criteria,ReadStrategy.none());
	}

	@Override
	public <T> Query<T> createQuery(Criteria<T> criteria, ReadStrategy strategy) {
		return storeManager.createQuery(criteria, metadataService.getStorageModel(criteria.getTargetClass()) ,strategy);
	}
	
	@Override
	public <T> T store(T obj) {
		Storable p;
		if (obj instanceof Storable){
			p = (Storable)obj;
		} else {
			// not managed yet
		    p = ProxyUtils.decorate(obj, Storable.class, new PersistableMethodHandler(obj.getClass()));
		}
		doStore(p);
		return (T)p;
	}
	
	private final void doStore(Storable p){
		switch (p.getPersistableState()){
		case DELETED:
			doDelete(p);
			break;
		case EDITED:
			doUpdate(p);
			break;
		case FILLED:
			doInsert(p);
			break;
		case BLANK:
		case RETRIVED:
			// not-op
			break;
		default:
			throw new IllegalStateException(p.getPersistableState() + " is unkown");
		}
	}

	private void doInsert(Storable p) {
		if (p.getIdentity()!=null){
			doUpdate(p);
		}
		
		// assign key
		p.setIdentity(this.storeManager.getSequence(p.getPersistableClass().getName()).next().getValue());
		
		this.storeManager.insert(Collections.singleton(p),metadataService.getStorageModel(p.getPersistableClass()));
		
		p.setPersistableState(PersistableState.RETRIVED);
	}

	private void doUpdate(Storable p) {
		if (p.getIdentity()==null){
			doInsert(p);
		}
		
		this.storeManager.update(Collections.singleton(p),metadataService.getStorageModel(p.getPersistableClass()));
		
		p.setPersistableState(PersistableState.RETRIVED);
	}

	private void doDelete(Storable p) {
		if (p.getIdentity()==null){
			return;
		}
		
		this.storeManager.remove(Collections.singleton(p),metadataService.getStorageModel(p.getPersistableClass()));
		
		p.setPersistableState(PersistableState.DELETED);
	}



	@Override
	public <T> void remove(T obj) {
		if (obj instanceof Storable){
			Storable p = (Storable)obj;
			p.setPersistableState(PersistableState.DELETED);
			doStore(p);
		} 
		// else 
		// not managed
		// do nothing as this obj is not in the store
		
	}

	@Override
	public <T> void remove(Criteria<T> criteria) {
		this.storeManager.remove(criteria, metadataService.getStorageModel(criteria.getTargetClass()));
	}

}

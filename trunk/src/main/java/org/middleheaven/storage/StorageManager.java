package org.middleheaven.storage;

import java.util.Collections;

import org.middleheaven.core.reflection.ProxyUtils;
import org.middleheaven.storage.criteria.Criteria;

public class StorageManager implements DataStorage {

	StoreManager storeManager;
	StoraMetadataManager metadataService;
	
	public StorageManager(StoreManager storeManager) {
		this.storeManager = storeManager;
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
		case EDITED:
			doUpdate(p);
		case FILLED:
			doInsert(p);
		case BLANK:
		case RETRIVED:
		default:
			// not-op
		}
	}

	private void doInsert(Storable p) {
		if (p.getKey()!=null){
			doUpdate(p);
		}
		
		// assign key
		p.setKey(this.storeManager.getSequence(p.getPersistableClass().getName()).next().getValue());
		
		this.storeManager.insert(Collections.singleton(p),metadataService.getStorageModel(p.getPersistableClass()));
		
		p.setPersistableState(PersistableState.RETRIVED);
	}

	private void doUpdate(Storable p) {
		if (p.getKey()==null){
			doInsert(p);
		}
		
		this.storeManager.update(Collections.singleton(p),metadataService.getStorageModel(p.getPersistableClass()));
		
		p.setPersistableState(PersistableState.RETRIVED);
	}

	private void doDelete(Storable p) {
		if (p.getKey()==null){
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
		// TODO Auto-generated method stub
		
	}

}

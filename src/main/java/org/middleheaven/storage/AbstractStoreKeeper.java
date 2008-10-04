package org.middleheaven.storage;

import org.middleheaven.core.reflection.ProxyUtils;

public abstract class AbstractStoreKeeper implements StoreKeeper {

	protected final void copy(Storable from, Storable to,StorableEntityModel model) {
		to.setIdentity(from.getIdentity());
		to.setPersistableState(from.getPersistableState());
		
		for (StorableFieldModel fm : model.fields()){
			to.setFieldValue(fm,from.getFieldValue(fm));
		}
	}
	
	protected final <T>  T merge(T obj){
		Storable p;
		if (obj instanceof Storable){
			p = (Storable)obj;
		} else {
			// not managed yet
			p = ProxyUtils.decorate(obj, Storable.class, new PersistableMethodHandler(obj.getClass()));
		}
		return (T)p;
	}

	
}

package org.middleheaven.storage;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.domain.EntityModel;
import org.middleheaven.util.identity.Identity;

public abstract class AbstractStoreKeeper implements StoreKeeper {

	private StorableModelReader reader;
	public AbstractStoreKeeper(StorableModelReader reader){
		this.reader = reader;
	}
	
	private final Map<String, StorableEntityModel> models = new HashMap<String, StorableEntityModel>();
	
	public final StorableEntityModel storableModelOf(EntityModel model){
		StorableEntityModel sm = models.get(model.getEntityName());
		if (sm==null){
			sm = reader.read(model);
			models.put(model.getEntityName(),sm);
		}
		return sm;
	}
	
	
	@Override
	public Identity getIdentityFor(Object object) {
		if (object instanceof Storable){
			return ((Storable)object).getIdentity();
		}
		return null;
	}
	
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
			p = ReflectionUtils.proxy(obj, Storable.class, new PersistableMethodHandler(obj.getClass()));
			ReflectionUtils.copy(obj, p);
		}
		@SuppressWarnings("unchecked") final T t = (T)p;
		return t;
	}

	
}

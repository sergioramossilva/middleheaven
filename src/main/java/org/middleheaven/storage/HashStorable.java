package org.middleheaven.storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.middleheaven.domain.EntityFieldModel;
import org.middleheaven.domain.EntityModel;
import org.middleheaven.util.identity.Identity;

public final class HashStorable implements Storable {

	private Map<EntityFieldModel , Object> values = new HashMap<EntityFieldModel, Object>();
	private Class<?> persistableClass;
	private StorableState state;
	private EntityModel model;
	
	public HashStorable (Class<?> persistableClass, EntityModel model){
		this.persistableClass = persistableClass;
		this.model = model;
	}
	
	@Override
	public Class<?> getPersistableClass() {
		return persistableClass;
	}
	
	@Override
	public StorableState getStorableState() {
		return state;
	}

	@Override
	public void setStorableState(StorableState state) {
		this.state = state;
	}
	
	@Override
	public EntityModel getEntityModel() {
		return model;
	}
	
	@Override
	public Object getFieldValue(EntityFieldModel model) {
		return this.values.get(model);
	}

	@Override
	public void setFieldValue(EntityFieldModel model, Object fieldValue) {
		this.values.put(model, fieldValue);
	}
	
	@Override
	public Identity getIdentity() {
		return (Identity)getFieldValue(model.identityFieldModel());
	}
	
	@Override
	public void setIdentity(Identity id) {
		setFieldValue(model.identityFieldModel(), id);
	}

	@Override
	public void removeFieldElement(EntityFieldModel model, Object element) {
		Collection<Object> collection = (Collection<Object>) getFieldValue(model);
		
		if ( collection != null){
			collection.remove(element);
		}
	}
	
	@Override
	public void addFieldElement(EntityFieldModel model, Object element) {
		Collection<Object> collection = (Collection<Object>) getFieldValue(model);
		
		if ( collection == null){
			collection = new LinkedList<Object>();
		}
		
		collection.add(element);
	}
	
	
	public static HashStorable clone(Storable storable){
		HashStorable h = new HashStorable(storable.getPersistableClass(), storable.getEntityModel());
		h.copyFrom(storable);
		return h;
	}
	
	public void copyFrom(Storable storable){
		
		for (EntityFieldModel fm : storable.getEntityModel().fields()){
			this.setFieldValue(fm, storable.getFieldValue(fm));
		}
	}

	public void copyTo(Storable storable){
		for (EntityFieldModel fm : storable.getEntityModel().fields()){
			storable.setFieldValue(fm, this.getFieldValue(fm));
		}
	}
}

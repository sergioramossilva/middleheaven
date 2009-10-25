package org.middleheaven.storage;

import org.middleheaven.domain.EntityFieldModel;
import org.middleheaven.domain.EntityModel;
import org.middleheaven.util.identity.Identity;

public interface Storable {

	public Class<?> getPersistableClass();
	
	public StorableState getStorableState();
	public void setStorableState(StorableState state);
	
	public Identity getIdentity();
	public void setIdentity(Identity id);
	
	public EntityModel getEntityModel();

	public Object getFieldValue(EntityFieldModel model);
	public void setFieldValue(EntityFieldModel model, Object fieldValue);
	public void addFieldElement(EntityFieldModel model, Object element);
	public void removeFieldElement(EntityFieldModel model, Object element);
}

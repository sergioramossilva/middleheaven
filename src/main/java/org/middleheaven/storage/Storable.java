package org.middleheaven.storage;

import org.middleheaven.util.identity.Identity;

public interface Storable {

	public Class<?> getPersistableClass();
	
	public StorableState getStorableState();
	public void setStorableState(StorableState state);
	
	public Identity getIdentity();
	public void setIdentity(Identity id);
	
	public Object getFieldValue(StorableFieldModel model);
	public void setFieldValue(StorableFieldModel model, Object fieldValue);
	public void addFieldElement(StorableFieldModel model, Object element);
	public void removeFieldElement(StorableFieldModel model, Object element);
}

package org.middleheaven.storage;

import org.middleheaven.util.identity.Identity;

public interface Storable {

	public PersistableState getPersistableState();
	public void setPersistableState(PersistableState state);
	public Identity getIdentity();
	public void setIdentity(Identity id);
	public Class<?> getPersistableClass();
	public Object getFieldValue(StorableFieldModel model);
	public void setFieldValue(StorableFieldModel model, Object fieldValue);
}

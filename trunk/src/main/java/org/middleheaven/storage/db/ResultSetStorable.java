package org.middleheaven.storage.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.middleheaven.storage.StorableState;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.StorageException;
import org.middleheaven.util.conversion.TypeConvertions;
import org.middleheaven.util.identity.Identity;

public class ResultSetStorable implements Storable {

	private ResultSet rs;
	private StorableEntityModel model;
	
	public ResultSetStorable(ResultSet rs, StorableEntityModel model) {
		this.rs = rs;
		this.model = model;
	}

	@Override
	public Identity getIdentity() {
		StorableFieldModel identityFieldModel = model.identityFieldModel();
		Object fieldValue = getFieldValue(identityFieldModel );
		return (Identity)TypeConvertions.convert(fieldValue, identityFieldModel.getValueClass());
	}
	
	@Override
	public Object getFieldValue(StorableFieldModel model) {
		try {
			Object value =  rs.getObject(model.getHardName().getName());
			if (model.getDataType().isReference()){
				return value;
			} else {
				return TypeConvertions.convert(value, model.getValueClass());
			}
		} catch (SQLException e) {
			throw new StorageException(e); 
		}
	}

	@Override
	public void setIdentity(Identity id) {
		throw new UnsupportedOperationException();
	}
	

	@Override
	public void setStorableState(StorableState state) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Class<?> getPersistableClass() {
		throw new UnsupportedOperationException();
	}

	@Override
	public StorableState getStorableState() {
		return StorableState.RETRIVED;
	}



	@Override
	public void setFieldValue(StorableFieldModel model, Object fieldValue) {
		throw new UnsupportedOperationException("This storable is read only");
	}

	@Override
	public void addFieldElement(StorableFieldModel model, Object element) {
		throw new UnsupportedOperationException("This storable is read only");
	}

	@Override
	public void removeFieldElement(StorableFieldModel model, Object element) {
		throw new UnsupportedOperationException("This storable is read only");
	}

}

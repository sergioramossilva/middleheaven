package org.middleheaven.storage.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.middleheaven.domain.EntityFieldModel;
import org.middleheaven.domain.EntityModel;
import org.middleheaven.storage.StorableEnum;
import org.middleheaven.storage.StorableEnumUtils;
import org.middleheaven.storage.StorableState;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StorableFieldModel;
import org.middleheaven.storage.StorageException;
import org.middleheaven.util.coersion.TypeCoercing;
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
		return (Identity)TypeCoercing.coerce(fieldValue, identityFieldModel.getValueType());
	}
	
	@Override
	public Object getFieldValue(EntityFieldModel fieldModel) {
		try {
			Object value =  rs.getObject(model.fieldModel(fieldModel.getLogicName()).getHardName().getName());
			if (fieldModel.getDataType().isReference()){
				return value;
			} else if (fieldModel.getDataType().isEnum()){
				Class<StorableEnum> storableEnum = (Class<StorableEnum>) fieldModel.getValueType();
				return StorableEnumUtils.valueForId(storableEnum, TypeCoercing.coerce(value, Integer.class));
			} else {
				return TypeCoercing.coerce(value, fieldModel.getValueType());
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
	public void setFieldValue(EntityFieldModel model, Object fieldValue) {
		throw new UnsupportedOperationException("This storable is read only");
	}

	@Override
	public void addFieldElement(EntityFieldModel model, Object element) {
		throw new UnsupportedOperationException("This storable is read only");
	}

	@Override
	public void removeFieldElement(EntityFieldModel model, Object element) {
		throw new UnsupportedOperationException("This storable is read only");
	}

	@Override
	public EntityModel getEntityModel() {
		return model;
	}

}

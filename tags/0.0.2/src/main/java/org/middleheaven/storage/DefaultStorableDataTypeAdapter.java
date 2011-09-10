package org.middleheaven.storage;

import org.middleheaven.model.domain.DataType;
import org.middleheaven.model.domain.DataTypeModel;

public class DefaultStorableDataTypeAdapter implements StorableDataTypeModel {

	
	private DataTypeModel model;

	public DefaultStorableDataTypeAdapter(DataTypeModel model){
		this.model = model;
	}
	
	@Override
	public DataType getDataType() {
		return model.getDataType();
	}

}

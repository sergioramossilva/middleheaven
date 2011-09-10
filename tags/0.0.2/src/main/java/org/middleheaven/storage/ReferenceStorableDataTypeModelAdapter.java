package org.middleheaven.storage;

import org.middleheaven.model.domain.DataType;
import org.middleheaven.model.domain.ReferenceDataTypeModel;

public class ReferenceStorableDataTypeModelAdapter implements ReferenceStorableDataTypeModel {

	private ReferenceDataTypeModel model;

	public ReferenceStorableDataTypeModelAdapter(ReferenceDataTypeModel model){
		this.model = model;
	}
	
	@Override
	public String getTargetFieldHardName() {
		return model.getTargetFieldName();
	}

	@Override
	public DataType getDataType() {
		return model.getDataType();
	}

	@Override
	public Class<?> getAggregationType() {
		return model.getAggregationType();
	}

	@Override
	public String getTargetFieldName() {
		return model.getTargetFieldName();
	}

	@Override
	public Class<?> getTargetFieldType() {
		return model.getTargetFieldType();
	}

	@Override
	public Class<?> getTargetType() {
		return model.getTargetType();
	}

}

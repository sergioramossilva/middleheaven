package org.middleheaven.storage;

import org.middleheaven.model.domain.ReferenceDataTypeModel;


public interface ReferenceStorableDataTypeModel extends StorableDataTypeModel , ReferenceDataTypeModel {

	
	public String getTargetFieldHardName();

//	public void setTargetFieldHardname(String fieldHardname);
//
//	public void setTargetFieldname(String fieldname);
//
//	public void setTargetType( Class<?> type);
//
//	public void setAggregationType(Class<?> type);
//	public void setTargetFieldType(Class<?> type);
}

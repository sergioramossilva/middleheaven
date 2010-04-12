package org.middleheaven.storage;

import org.middleheaven.domain.ReferenceDataTypeModel;


public interface ReferenceStorableDataTypeModel extends StorableDataTypeModel , ReferenceDataTypeModel {

	
	public String getTargetFieldHardName();

	public void setTargetFieldHardname(String fieldHardname);

	public void setTargetFieldname(String fieldname);

	
}

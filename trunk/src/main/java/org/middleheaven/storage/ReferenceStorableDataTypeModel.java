package org.middleheaven.storage;


public interface ReferenceStorableDataTypeModel extends StorableDataTypeModel {

	
	public String getTargetFieldHardName();

	public void setTargetFieldHardname(String fieldHardname);

	
	public String getTargetFieldName();

	public void setTargetFieldname(String fieldname);

	
}

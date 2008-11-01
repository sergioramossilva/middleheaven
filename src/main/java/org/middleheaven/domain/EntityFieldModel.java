package org.middleheaven.domain;

import org.middleheaven.data.DataType;
import org.middleheaven.storage.QualifiedName;

public interface EntityFieldModel {

	public QualifiedName getLogicName();
	public DataType getDataType();
	public boolean isTransient();
	public boolean isVersion();
	public boolean isKey();
	public Class<?> getValueClass();
	public boolean isUnique();
	
	
}

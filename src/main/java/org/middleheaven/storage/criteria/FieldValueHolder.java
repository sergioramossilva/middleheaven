package org.middleheaven.storage.criteria;

import java.io.Serializable;

import org.middleheaven.domain.DataType;

public interface FieldValueHolder extends Serializable{

	public boolean isEmpty();
	public Object getValue();
	public DataType getDataType();
	public void setDataType(DataType type);
	public boolean equalsValue(FieldValueHolder valueHolder);

}

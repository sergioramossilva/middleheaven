package org.middleheaven.util.criteria;

import java.io.Serializable;

import org.middleheaven.domain.DataType;

public interface FieldValueHolder extends Serializable{

	public boolean isEmpty();
	public Object getValue();
	public DataType getDataType();
	public void setDataType(DataType type);
	public boolean equalsValue(FieldValueHolder valueHolder);
	
	public String getParam(String string);
	public void setParam(String string, String param);

}

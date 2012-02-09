package org.middleheaven.util.criteria;

import java.io.Serializable;

public interface FieldValueHolder extends Serializable{

	public boolean isEmpty();
	public Object getValue();
	
	public boolean equalsValue(FieldValueHolder valueHolder);
	
	public String getParam(String string);
	public void setParam(String string, String param);

}

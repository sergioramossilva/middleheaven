package org.middleheaven.util.criteria;

import java.util.Map;

import org.apache.tools.ant.types.DataType;
import org.middleheaven.util.collections.ParamsMap;

public class SingleObjectValueHolder implements FieldValueHolder {

	private static final long serialVersionUID = 2756122283381728051L;
	
	private Object value;
	private DataType dataType;
	private Map<String,String> params = new ParamsMap();
	
	public SingleObjectValueHolder(Object value,DataType dataType) {
		this.value = value;
		this.dataType = dataType;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public DataType getDataType() {
		return dataType;
	}

	@Override
	public void setDataType(DataType type) {
		dataType = type;
	}

	@Override
	public boolean equalsValue(FieldValueHolder valueHolder) {
		return value.equals(valueHolder.getValue());
	}


	@Override
	public String getParam(String name) {
		return params.get(name);
	}

	@Override
	public void setParam(String name, String value) {
		params.put(name, value);
	}

}

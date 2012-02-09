package org.middleheaven.util.criteria;

import java.util.Map;

import org.middleheaven.util.collections.ParamsMap;

public class SingleObjectValueHolder implements FieldValueHolder {

	private static final long serialVersionUID = 2756122283381728051L;
	
	private Object value;
	private Map<String,String> params = new ParamsMap();
	
	public SingleObjectValueHolder(Object value) {
		this.value = value;
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

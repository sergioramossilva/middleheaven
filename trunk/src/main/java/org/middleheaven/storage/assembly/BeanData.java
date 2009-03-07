/**
 * 
 */
package org.middleheaven.storage.assembly;

final class BeanData extends Data{

	Class<?> type;
	public BeanData(String name, Object value, Class<?> type) {
		super(name, value);
		this.type = type;
	}
	
	public Class<?> getValueType(){
		return type;
	}
	
}
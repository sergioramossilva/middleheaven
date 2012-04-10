package org.middleheaven.core.wiring;

import java.util.Map;

import org.middleheaven.util.collections.CollectionUtils;

class Key {

	private Class targetClass;
	private Map<String, Object> params;

	public String toString(){
		return targetClass.getName() + "+" + params.toString();
	}
	
	public static  Key keyFor(Class<?> targetClass, Map<String, Object> params) {
		return new Key(targetClass,params);
	}
	
	private Key(Class<?> targetClass, Map<String, Object> params) {
		this.targetClass = targetClass;
		this.params = params;	
	}
	
	public int hashCode(){
		return targetClass==null ? 0 :  targetClass.getName().hashCode();
	}
	
	public boolean equals(Object other){
		return other instanceof Key && equals((Key)other);
	}
	
	public boolean equals(Key other){
		return (other.targetClass == this.targetClass ||  other.targetClass.getName().equals(this.targetClass.getName())) &&
		 	CollectionUtils.equalContents(this.params, other.params);

	}

}

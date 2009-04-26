package org.middleheaven.util;

import java.util.Arrays;

public final class ArrayMapKey {

	private Object[] objects;

	public ArrayMapKey(Object ... objects){
		this.objects = objects;
	}
	
	public Object[] getObjects(){
		return this.objects;
	}
	
	public boolean equals(Object other){
		return other instanceof ArrayMapKey && Arrays.equals(((ArrayMapKey)other).objects , this.objects);
	}
	
	public int hashCode(){
		return Hash.hash(objects).hashCode();
	}
}

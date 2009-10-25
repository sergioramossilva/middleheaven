package org.middleheaven.util.identity;

import java.io.Serializable;


public final class StringIdentity extends Identity implements Serializable {

	private final String value;
	
	public static StringIdentity valueOf(String value){
		return value == null ? null : new StringIdentity(value);
	}
	
	private StringIdentity(String value){
		this.value = value;
	}
	
	@Override
	public boolean equals(Identity other) {
		return other instanceof StringIdentity && ((StringIdentity)other).value.equals(this.value);
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public int compareTo(Identity other) {
		return this.value.compareTo(((StringIdentity)other).value);
	}

	@Override
	public String toString() {
		return value.toString();
	}


}

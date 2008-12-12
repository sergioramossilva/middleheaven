package org.middleheaven.util.identity;

import java.io.Serializable;


public class StringIdentity extends Identity implements Serializable {

	String value;
	
	public static StringIdentity valueOf(String value){
		return new StringIdentity(value);
	}
	
	public StringIdentity(String value){
		this.value = value;
	}
	
	@Override
	public boolean equals(Identity other) {
		return other instanceof StringIdentity && equals((StringIdentity)other);
	}
	
	public boolean equals(StringIdentity other) {
		return this.value == other.value;
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

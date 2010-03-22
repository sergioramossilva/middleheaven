package org.middleheaven.util.identity;

import java.io.Serializable;
import java.util.UUID;

public class UUIDIdentity extends Identity implements Serializable {

	private UUID value;
	
	public static UUIDIdentity valueOf(String value) {
		return new UUIDIdentity(value);
	}
	
	public static UUIDIdentity next() {
		return new UUIDIdentity(UUID.randomUUID());
	}
	
	public UUIDIdentity(UUID value){
		this.value = value;
	}
	
	public UUIDIdentity(String value){
		this.value = UUID.fromString(value);
	}
	
	@Override
	public boolean equals(Identity other) {
		return other instanceof UUIDIdentity && equals((UUIDIdentity)other);
	}
	
	public boolean equals(UUIDIdentity other) {
		return this.value == other.value;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public String toString() {
		return value.toString();
	}



}

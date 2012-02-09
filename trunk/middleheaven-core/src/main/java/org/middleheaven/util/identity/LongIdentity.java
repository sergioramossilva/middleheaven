package org.middleheaven.util.identity;

import java.io.Serializable;

import org.middleheaven.util.Hash;

public class LongIdentity extends Identity implements Serializable {


	private long value;

	public static LongIdentity valueOf(String value) {
		return valueOf(Long.parseLong(value));
	}

	public static LongIdentity valueOf(Long value) {
		return  valueOf(value.longValue());
	}
	
	public static LongIdentity valueOf(long value) {
		return new LongIdentity(value);
	}
	
	public LongIdentity(long value) {
		this.value = value;
	}

	@Override
	public boolean equals(Identity other) {
		return other instanceof LongIdentity && equals((LongIdentity)other);
	}

	protected boolean equals(LongIdentity other) {
		return this.value == other.value;
	}

	@Override
	public int hashCode() {
		return Hash.hash(value).hashCode();
	}

	@Override
	public String toString() {
		return Long.toString(value);
	}

	public long longValue(){
		return value;
	}

	public LongIdentity next(){
		return new LongIdentity(this.value+1);
	}


}

package org.middleheaven.util.identity;

import java.io.Serializable;

import org.middleheaven.util.Hash;

public class LongIdentity extends Identity implements Serializable {


	private long value;

	public static Identity valueOf(String value) {
		return new LongIdentity(Integer.parseInt(value));
	}

	public LongIdentity(long l) {
		this.value = l;
	}

	@Override
	public boolean equals(Identity other) {
		return other instanceof LongIdentity && equals((LongIdentity)other);
	}

	public boolean equals(LongIdentity other) {
		return this.value == other.value;
	}

	@Override
	public int hashCode() {
		return Hash.hash(value).hashCode();
	}

	@Override
	public int compareTo(Identity other) {
		if ( this.value  == ((LongIdentity)other).value ){
			return 0; 
		} else if (this.value  > ((LongIdentity)other).value ){
			return 1;
		} else {
			return -1;
		}
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

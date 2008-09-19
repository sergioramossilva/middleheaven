package org.middleheaven.util.identity;

public class IntegerIdentity extends Identity {

	
	private int value;

	public IntegerIdentity(int value) {
		this.value = value;
	}

	@Override
	public boolean equals(Identity other) {
		return other instanceof IntegerIdentity && equals((IntegerIdentity)other);
	}
	
	public boolean equals(IntegerIdentity other) {
		return this.value == other.value;
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public int compareTo(Identity other) {
		return this.value - ((IntegerIdentity)other).value;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}

	public int intValue(){
		return value;
	}
	
	public IntegerIdentity next(){
		return new IntegerIdentity(++this.value);
	}
}

package org.middleheaven.storage;

import org.middleheaven.util.Hash;

public class ReadStrategy {

	private boolean isReadOnly = false;
	private boolean isFowardOnly = false;

	public static ReadStrategy editableRandomAccess(){
		return new ReadStrategy(false,false);
	}

	public static ReadStrategy fowardReadOnly(){
		return new ReadStrategy(true,true);
	}

	private ReadStrategy(boolean isReadOnly, boolean isFowardOnly) {
		this.isReadOnly = isReadOnly;
		this.isFowardOnly = isFowardOnly;
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}
	public boolean isFowardOnly() {
		return isFowardOnly;
	}

	public ReadStrategy readOnly(){
		return new ReadStrategy(true,this.isFowardOnly);
	}

	public ReadStrategy fowardOnly(){
		return new ReadStrategy(this.isReadOnly,true);
	}

	public final boolean equals(Object other){
		return other instanceof ReadStrategy && equals((ReadStrategy) other);
	}

	public int hashCode(){
		return Hash.hash(isFowardOnly).hash(isReadOnly).hashCode();
	}

	private boolean equals(ReadStrategy other){
		return this.isFowardOnly == other.isFowardOnly &&
				this.isReadOnly == other.isReadOnly;
	}

	@Override
	public String toString() {
		return "ReadStrategy [isFowardOnly=" + isFowardOnly + ", isReadOnly="
				+ isReadOnly + "]";
	}


}

package org.middleheaven.util.identity;

import java.io.Serializable;

public abstract class Identity implements Serializable{


	public final boolean equals(Object other){
		return other !=null && this.getClass().isAssignableFrom(other.getClass()) && equals((Identity)other);
	}
	
	public abstract int hashCode();
	
	protected abstract boolean equals(Identity other);
	
	public abstract String toString();
	
}
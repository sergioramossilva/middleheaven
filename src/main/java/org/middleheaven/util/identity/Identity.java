package org.middleheaven.util.identity;

import java.io.Serializable;

public abstract class Identity implements Comparable<Identity> , Serializable{


	public boolean equals(Object other){
		return other !=null && this.getClass().isAssignableFrom(other.getClass()) && equals((Identity)other);
	}
	
	public abstract int hashCode();
	
	public abstract boolean equals(Identity other);
	
	public abstract String toString();
	
}

package org.middleheaven.util.identity;

public abstract class Identity implements Comparable<Identity>{


	public boolean equals(Object other){
		return other !=null && this.getClass().isAssignableFrom(other.getClass()) && equals((Identity)other);
	}
	
	public abstract int hashCode();
	
	public abstract boolean equals(Identity other);
	
	public abstract String toString();
	
}

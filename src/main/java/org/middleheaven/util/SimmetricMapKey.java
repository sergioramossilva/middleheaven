package org.middleheaven.util;


public class SimmetricMapKey<T> {

	
	private  Object A; 
	private  Object B;
	
	public SimmetricMapKey(Object a, Object b) {
		A = a;
		B = b;
	}
	
	public int hashCode(){
		return A.hashCode() ^ B.hashCode();
	}
	
	public boolean equals(Object other) {
		return other instanceof SimmetricMapKey && equals((SimmetricMapKey<?>) other);
	}

	public boolean equals(SimmetricMapKey<T> other) {
		 return (A.equals(other.A) &&  B.equals(other.B)) || (A.equals(other.B) &&  B.equals(other.A));
	}
}

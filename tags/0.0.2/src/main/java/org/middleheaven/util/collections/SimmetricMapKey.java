package org.middleheaven.util.collections;

/**
 * MapKey composed by two objects.
 * SimmetricMapKey is equals to another SimmetricMapKey if 
 * the objects are the same. More generally they are considered equals 
 * if SimmetricMapKey A holds X and Y and SimmetricMapKey B holds Y and X 
 * 
 *
 * @param <T>
 */
public final class SimmetricMapKey extends ComposedMapKey{

	
	private  Object a; 
	private  Object b;
	
	public SimmetricMapKey(Object a, Object b) {
		this.a = a;
		this.b = b;
	}
	
	public int hashCode(){
		return a.hashCode() ^ b.hashCode();
	}
	
	public boolean equals(Object other) {
		return other instanceof SimmetricMapKey && equals((SimmetricMapKey) other);
	}

	public boolean equals(SimmetricMapKey other) {
		 return (a.equals(other.a) &&  b.equals(other.b)) || (a.equals(other.b) &&  b.equals(other.a));
	}
}

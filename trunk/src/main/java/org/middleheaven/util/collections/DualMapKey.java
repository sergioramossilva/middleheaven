package org.middleheaven.util.collections;


/**
 * Holds two values as a single key. 
 * The values must be set in the same order or the 
 * key will be different. Null is not accepted
 * 
 * @See SimetricMapKey
 */
public final class DualMapKey<A,B> extends ComposedMapKey {

	public static <X,Y> DualMapKey<X,Y> of(X x, Y y) {
		return new DualMapKey<X,Y>(x,y);
	}

	
	private A a;
	private B b;

	private DualMapKey (A a , B b){
		if(a==null || b ==null){
			throw new IllegalArgumentException("null value is not acceped for dual keys");
		}
		this.a = a;
		this.b = b;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof DualMapKey<?,?> && 
		((DualMapKey<?,?>) obj).a.equals(a) && 
		((DualMapKey<?,?>) obj).b.equals(b);
	}

	@Override
	public int hashCode() {
		return 31 * a.hashCode() +  b.hashCode();
	}


}

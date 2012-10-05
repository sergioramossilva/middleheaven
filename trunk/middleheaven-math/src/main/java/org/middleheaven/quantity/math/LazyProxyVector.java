/**
 * 
 */
package org.middleheaven.quantity.math;

import org.middleheaven.quantity.math.structure.Field;

/**
 * 
 */
abstract class LazyProxyVector<F extends Field<F>> extends AbstractVector<F> {

	private final int size;
	private final Object[] cache;
	
	/**
	 * Constructor.
	 * @param provider
	 */
	public LazyProxyVector(VectorSpaceProvider provider, int size) {
		super(provider);
		this.size = size;
		this.cache = new Object[size];
		
	}


	@Override
	public final F get(int index) {
		if (index >= this.size() || index < 0){
			throw new IndexOutOfBoundsException("Index " + index + ", Size: " + this.size());
		}
		
		Object value = cache[index]; 
		if (value == null){

			value = lazyGet(index);
			cache[index] = value;
		}
		
		return (F) value;
	}
	

	protected abstract F lazyGet(int index);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return size;
	}

}

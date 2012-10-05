/**
 * 
 */
package org.middleheaven.quantity.math;

import java.util.ArrayList;
import java.util.List;

import org.middleheaven.quantity.math.structure.Field;

/**
 * 
 */
class LazyDelegationVector<F extends Field<F>> extends AbstractVector<F> {

	
	private Object[] cache;
	private Vector<F> original;
	private ValueResolver<F> resolver;
	
	protected LazyDelegationVector(Vector<F> original, VectorSpaceProvider provider, ValueResolver<F> resolver){
		super(provider);
		this.original = original;
		this.resolver = resolver;
		this.cache = new Object[original.size()];
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public F get(int index) {
		
		if (index >= this.size()){
			throw new IndexOutOfBoundsException("Index " + index + ", Size: " + this.size());
		}
		
		Object value = cache[index]; 
		if (value == null){

			value = resolver.resolve(index, original);
			cache[index] = value;
		}
		
		return (F) value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return this.original.size();
	}




}

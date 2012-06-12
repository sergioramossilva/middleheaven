package org.middleheaven.quantity.math.structure;

public interface VectorSpace<V,F extends Field<F>> extends GroupAdditive<V>{

	/**
	 * Multiplication by a scalar
	 * @param a scalar value
	 * @return this multiplied by <code>a</code>. A new vector is returned where each coordinate is multiplied by <code>a</code>. 
	 * 
	 */
	public V times (F a);
}

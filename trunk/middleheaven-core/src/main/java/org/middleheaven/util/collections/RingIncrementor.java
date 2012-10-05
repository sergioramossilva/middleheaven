/**
 * 
 */
package org.middleheaven.util.collections;

import org.middleheaven.quantity.math.structure.Ring;
import org.middleheaven.util.Incrementor;

/**
 * 
 */
class RingIncrementor<V extends Ring<V>> implements Incrementor<V> {

	private V step;

	public RingIncrementor (V step){
		this.step = step;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public V increment(V object) {
		return object.plus(step);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Incrementor<V> reverse() {
		return new RingIncrementor<V>(step.negate());
	}

}

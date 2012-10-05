/**
 * 
 */
package org.middleheaven.quantity.math;

import org.middleheaven.quantity.math.structure.Field;

/**
 * 
 */
abstract class ProxyVector<F extends Field<F>> extends AbstractVector<F> {

	private final int size;

	/**
	 * Constructor.
	 * @param provider
	 */
	public ProxyVector(VectorSpaceProvider provider, int size) {
		super(provider);
		this.size = size;
		
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return size;
	}

}

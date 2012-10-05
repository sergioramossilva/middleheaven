/**
 * 
 */
package org.middleheaven.quantity.math;

import org.middleheaven.quantity.math.structure.Field;

/**
 * 
 */
public abstract class ProxyMatrix<F extends Field<F>> extends AbstractMatrix<F> {


	private int rows;
	private int columns;

	public ProxyMatrix (VectorSpaceProvider provider, int rows, int columns){
		super(provider);
		this.rows = rows;
		this.columns = columns;
	}
	


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int rowsCount() {
		return rows;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int columnsCount() {
		return columns;
	}

}

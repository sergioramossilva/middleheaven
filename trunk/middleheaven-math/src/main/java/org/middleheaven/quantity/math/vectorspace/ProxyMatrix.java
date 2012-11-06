/**
 * 
 */
package org.middleheaven.quantity.math.vectorspace;

import org.middleheaven.quantity.math.structure.FieldElement;

/**
 * 
 */
public abstract class ProxyMatrix<F extends FieldElement<F>> extends AbstractMatrix<F> {


	private int rows;
	private int columns;

	public ProxyMatrix (VectorSpace provider, int rows, int columns){
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

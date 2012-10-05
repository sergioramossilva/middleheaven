/**
 * 
 */
package org.middleheaven.quantity.math;

import org.middleheaven.quantity.math.structure.Field;

/**
 * 
 */
public abstract class LazyProxyMatrix<F extends Field<F>> extends AbstractMatrix<F> {


	private int rows;
	private int columns;
	private Object[] cache;

	public LazyProxyMatrix (VectorSpaceProvider provider, int rows, int columns){
		super(provider);
		this.rows = rows;
		this.columns = columns;
		this.cache = new Object[rows * columns];
	}
	

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final F get(int r, int c ){
		
		if (r >= this.rowsCount() || r < 0){
			throw new IndexOutOfBoundsException("Row Index " + r + ", Size: " + this.rowsCount());
		}
		
		if (c >= this.columnsCount() || c < 0){
			throw new IndexOutOfBoundsException("Column Index " + c + ", Size: " + this.columnsCount());
		}
		
		final Integer index = indexOf(r,c);
		F value = (F) cache[index]; 
		if (value != null){
			return value;
		} else {
			
			value = lazyGet(r, c);
			cache[index] = value;
		}
		
		return value;
	}
	
	private Integer indexOf(int r, int c) {
		return Integer.valueOf(r * this.rowsCount() + c);
	}
	
	
	protected abstract F lazyGet(int r, int c );

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

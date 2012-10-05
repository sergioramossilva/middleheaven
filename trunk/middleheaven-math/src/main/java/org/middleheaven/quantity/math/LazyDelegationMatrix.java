/**
 * 
 */
package org.middleheaven.quantity.math;

import org.middleheaven.quantity.math.structure.Field;

/**
 * 
 */
class LazyDelegationMatrix<F extends Field<F>> extends LazyProxyMatrix<F> {

	

	private CellResolver<F> cellResolver;
	private Matrix<F> original;
	public LazyDelegationMatrix(Matrix<F> original, VectorSpaceProvider provider) {
		this(original, provider, new CellResolver<F>(){
			
			public F resolve (int r, int c, Matrix<F> original){
				return original.get(r,c);
			}
			
		});
	}
	/**
	 * Constructor.
	 * @param provider
	 */
	public LazyDelegationMatrix(Matrix<F> original, VectorSpaceProvider provider, CellResolver<F> cellResolver) {
		super(provider, original.rowsCount(), original.columnsCount());
		this.cellResolver =  cellResolver;
		this.original = original;
		
	
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final F lazyGet(int r, int c) {
		return  cellResolver.resolve(r, c, original);

	}

	


	

	
	
}

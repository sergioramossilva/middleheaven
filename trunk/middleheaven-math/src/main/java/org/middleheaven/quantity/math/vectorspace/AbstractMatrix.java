package org.middleheaven.quantity.math.vectorspace;

import org.middleheaven.quantity.math.Conjugatable;
import org.middleheaven.quantity.math.UnivariateFunction;
import org.middleheaven.quantity.math.structure.DefaultMatrixInvertion;
import org.middleheaven.quantity.math.structure.Field;
import org.middleheaven.quantity.math.structure.FieldElement;
import org.middleheaven.quantity.math.structure.Ring;

/**
 * Simple base implementation for a {@link Matrix}.
 * @param <F> the {@link FieldElement} type of the elements of the matrix.
 */
public abstract class AbstractMatrix<F extends FieldElement<F>> implements Matrix<F>{


	
	private VectorSpace<Vector<F>, F> provider;

	public AbstractMatrix(VectorSpace<Vector<F>, F> provider) {
		this.provider = provider;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ring<Matrix<F>> getAlgebricStructure() {
		return new Ring<Matrix<F>> (){

			@Override
			public Matrix<F> zero() {
				throw new UnsupportedOperationException("Not implememented yet");
			}

			@Override
			public boolean isGroupAdditive() {
				return true;
			}

			@Override
			public boolean isRing() {
				return false;
			}

			@Override
			public boolean isField() {
				return false;
			}

			@Override
			public Matrix<F> one() {
				return provider.identity(Math.min(rowsCount(), columnsCount()));
			}
			
		};
	}

	
	
	protected final VectorSpace<Vector<F>, F> getMatrixProvider(){
		return provider;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public Field<F> getField(){
		return provider.getField();
	}
	
	public boolean isSquare(){
		return this.rowsCount() == this.columnsCount();
	}
	
	public boolean isSimmetric(){
		if (this.rowsCount()==1){
			return true;
		} else if (!this.isSquare()){
			return false;
		} 
		
		return this.equalsOther(this.transpose());
	}
	
	
	public Vector<F> getRow(final int row){
		
		return new ProxyVector<F>(this.provider, this.columnsCount()){

			@Override
			public F get(int index) {
				return AbstractMatrix.this.get(row,index);
			}
			
		};
		
	}

	public Vector<F> getColumn(final int column){
		
		return new ProxyVector<F>(this.provider, this.rowsCount()){

			@Override
			public F get(int index) {
				return AbstractMatrix.this.get(index, column);
			}
			
		};

	}

	public Vector<F> getDiagonal(){
		
		return new ProxyVector<F>(this.provider, this.rowsCount()){

			@Override
			public F get(int index) {
				return AbstractMatrix.this.get(index, index);
			}
			
		};

	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasInverse() {
		F zero = this.getMatrixProvider().getField().zero();
		return this.isSquare() && !this.determinant().equals(zero);
	}
	
	/**
	 * 
	 * @return the inverse matrix.
	 */
	public Matrix<F> inverse() {

		if (hasInverse()){
			return new DefaultMatrixInvertion().invert(this);
		} else {
			throw new ArithmeticException("This matrix has no inverse");
		}

	}
	
	@Override
	public final Matrix<F> minus(final Matrix<F> other) {
		return new LazyDelegationMatrix<F>(this, this.getMatrixProvider(), new CellResolver<F>(){

			@Override
			public F resolve(int r, int c, Matrix<F> original) {
				return original.get(r, c).minus(other.get(r, c));
			}
			
		});
	}
	
	@Override
	public final Matrix<F> negate() {
		
		return new LazyDelegationMatrix<F>(this, this.getMatrixProvider(), new CellResolver<F>(){

			@Override
			public F resolve(int r, int c, Matrix<F> original) {
				return original.get(r, c).negate();
			}
			
		});
		
	}

	@Override
	public final Matrix<F> plus(final Matrix<F> other) {
		
		return new LazyDelegationMatrix<F>(this, this.getMatrixProvider(), new CellResolver<F>(){

			@Override
			public F resolve(int r, int c, Matrix<F> original) {
				return original.get(r, c).plus(other.get(r, c));
			}
			
		});
	}

	public Matrix<F> times(final F a) {
		
		return new LazyDelegationMatrix<F>(this, this.getMatrixProvider(), new CellResolver<F>(){

			@Override
			public F resolve(int r, int c, Matrix<F> original) {
				return original.get(r, c).times(a);
			}
			
		});
	}
	
	@Override
	public Matrix<F> adjoint() {

		return new LazyDelegationMatrix<F>(this, this.getMatrixProvider(), new CellResolver<F>(){

			@Override
			public F resolve(int r, int c, Matrix<F> original) {
				return original.cofactor(r, c);
			}
			
		});
		
	}
	
	
	@Override
	public final Matrix<F> conjugate() {

			return new LazyDelegationMatrix<F>(this, this.getMatrixProvider(), new CellResolver<F>(){

				@SuppressWarnings("unchecked")
				@Override
				public F resolve(int r, int c, Matrix<F> original) {
					F value = original.get(r, c);
					
					if (value instanceof Conjugatable){
						return ((Conjugatable<F>) value).conjugate();
					} else {
						return value;
					}
				}
				
			});
	}


	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Vector<F> rightTimes(final Vector<F> vector) {
		
		return new ProxyVector<F>(this.provider, this.columnsCount()){

			@Override
			public F get(int index) {
				return getColumn(index).times(vector);
			}
			
		};
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Vector<F> leftTimes(final Vector<F> vector) {
		
		return new ProxyVector<F>(this.provider, this.columnsCount()){

			@Override
			public F get(int index) {
				return getRow(index).times(vector);
			}
			
		};
	}
	
	@Override
	public Matrix<F> times(final Matrix<F> other) {

		return new LazyDelegationMatrix<F>(this, this.getMatrixProvider(), new CellResolver<F>(){

			@Override
			public F resolve(int r, int c, Matrix<F> original) {
					
				// the value for r, c is the linear combination of the original row, with the other column
				// T = A * B
				// T[r,c] = A[r,i] * B[i,c]
				
				return original.getRow(r).times(other.getColumn(c));
				
			}
			
		});

	}
	
	
	@Override
	public F determinant() {
		return determinant(this);
	}

	
	private F determinant(Matrix<F> mat) {

		if (!mat.isSquare()){
			throw new ArithmeticException("Matriz is not square");
		}

		if(mat.rowsCount() == 1) {
			return mat.get(0,0);
		}

		if(mat.rowsCount() == 2) { 
			return mat.get(0,0).times(mat.get(1,1)).minus(mat.get(0,1).times(mat.get(1,0))); 
		}

		F zero = this.provider.getField().zero();
		
		F result = zero;
		Vector<F> masterRow =  mat.getRow(0);

		for(int i = 0; i < masterRow.size(); i++) { 
			Matrix<F> temp = mat.remove(0,i);

			F x = masterRow.get(i);
			if (!x.equals(zero)){
				if( Math.pow(-1, i)==-1){
					result = result.plus(determinant(temp).times(x.negate())); 
				} else {
					result = result.plus(determinant(temp).times(x)); 
				}
			}
		}
		return result; 

	} 
	
	/**
	 * Return a matrix where the r row and the c column have bean removed
	 * @param row index of the row to remove
	 * @param column index of the column to remove.
	 * @return
	 */
	public Matrix<F> remove(final int row, final int column) {
		
		return new LazyDelegationMatrix<F>(this, this.getMatrixProvider(), new CellResolver<F>(){

			@Override
			public F resolve(int r, int c, Matrix<F> original) {
					
				final int correctRow = r >= row ? r +1 : r;
				final int correctColumn = c >= column ? c +1 : c;
				
				return original.get(correctRow, correctColumn);
			}
				
		}){
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int rowsCount() {
			return super.rowsCount() - 1;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int columnsCount() {
			return super.columnsCount() - 1;
		}
				
		};
		
	}
	
	@Override
	public final F cofactor(int r, int c) {
		// the determinant of the matriz by removing this row and columns
		if( Math.pow(-1, r+c)==-1){
			return this.remove(r, c).determinant().negate();
		} else {
			return this.remove(r, c).determinant(); 
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public Matrix<F> transpose() {
		return new TransposedMatrix<F>(this, this.provider);
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final F trace(){
		F result = this.get(0, 0);
		for (int i=1; i<this.rowsCount();i++){
			result = result.plus(this.get(i,i));
		}
		return result;
	}


	/**
	 * 
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public boolean equals(Object other){
		return other instanceof Matrix && equalsOther((Matrix<F>)other); 
	}
	
	private boolean equalsOther(Matrix<?> other){
		if (this.rowsCount()!= other.rowsCount() || this.columnsCount() != this.columnsCount()){
			return false;
		}
		
		for (int r =0; r < this.rowsCount(); r++){
			for (int c =0; c < this.columnsCount(); c++){
				if (!this.get(r, c).equals(other.get(r, c))){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public int hashCode(){
		return this.columnsCount() + 13 * this.rowsCount();
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public String toString(){
		StringBuilder text = new StringBuilder("\n");
		for (int r =0; r < this.rowsCount(); r++){
			for (int c =0; c < this.columnsCount(); c++){
				text.append(this.get(r, c).toString()).append(",");
			}
			text.delete(text.length()-1,text.length());
			text.append("\n");
		}
		return text.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isZero() {
		return this.equals(this.getAlgebricStructure().zero());
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public <N extends FieldElement<N>> Matrix<N> apply(final UnivariateFunction<F, N> function) {
		return new LazyProxyMatrix<N>(this.provider, this.rowsCount(), this.columnsCount()) {

			@Override
			public N lazyGet(int r, int c) {
				return function.apply(AbstractMatrix.this.get(r, c));
			}
			
		};
	}
}

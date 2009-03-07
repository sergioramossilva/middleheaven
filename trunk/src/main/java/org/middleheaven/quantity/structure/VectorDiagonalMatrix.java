package org.middleheaven.quantity.structure;

class VectorDiagonalMatrix<F extends Field<F>> extends DiagonalMatrix<F> {

	protected F zero;
	
	protected Vector<F> diag;
	

    VectorDiagonalMatrix( F ... elements){
		this.diag = DenseVector.vector(elements);
		
		this.zero = elements[0].minus(elements[0]);
	}
	
    
	VectorDiagonalMatrix( Vector<F> vector){
	 
		this.diag = DenseVector.vector(vector);
		
		this.zero = diag.get(0).minus(diag.get(0));
	}
	
	@Override
	public int columnsCount() {
		return diag.getDimention();
	}

	@Override
	public int rowsCount() {
		return diag.getDimention();
	}

	@Override
	public F determinant() {
		F product = diag.get(0);
		for (int i=1 ; i < diag.getDimention(); i++){
			product = product.times(diag.get(1));
		}
		return product;
	}

	@Override
	public F get(int r, int c) {
		return r==c ? diag.get(r) : zero;
	}
	
	@Override
	public Matrix<F> negate() {
		return new VectorDiagonalMatrix<F>(diag.negate());
	}
	
	@Override
	public Matrix<F> times(F a) {
		return new VectorDiagonalMatrix<F>(diag.times(a));
	}

	@Override
	public Matrix<F> adjoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public F cofactor(int r, int c) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Vector<F> getDiagonal() {
		return diag;
	}


	public boolean isSquare(){
		return true;
	}
	
	@Override
	public boolean hasInverse() {
		return !this.determinant().equals(zero);
	}

	@Override
	public Matrix<F> inverse() {
		return this;
	}



	@Override
	public Matrix<F> transpose() {
		return this;
	}


	@Override
	public Vector<F> times(Vector<F> vector) {
		return vector;
	}

	@Override
	public Matrix<F> plus(Matrix<F> other) {
		if (other instanceof SingleValueVectorDiagonalMatrix){
			return new VectorDiagonalMatrix<F>(diag.plus(((VectorDiagonalMatrix<F>)other).diag));
		} else {
			return other.plus(this);
		}
	}

	

	
}

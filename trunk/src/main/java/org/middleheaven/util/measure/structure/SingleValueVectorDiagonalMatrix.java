package org.middleheaven.util.measure.structure;

public class SingleValueVectorDiagonalMatrix<F extends Field<F>> extends DiagonalMatrix<F> {

	private F value;
	private F zero;
	private int size;

	public SingleValueVectorDiagonalMatrix(int size, F value) {
		this.size = size;
		this.value = value;
		this.zero = value.zero();
	}


	@Override
	public int columnsCount() {
		return size;
	}

	@Override
	public int rowsCount() {
		return size;
	}

	@Override
	public F determinant() {
		if (value.times(value).equals(value)){
			// value is ONE
			return value;
		} else {
			F product = value;
			for (int i=1 ; i < size; i++){
				product = product.times(value);
			}
			return product;
		}
	}

	@Override
	public F get(int r, int c) {
		return r==c ? value : zero;
	}

	@Override
	public Matrix<F> negate() {
		return new SingleValueVectorDiagonalMatrix<F>(size,value.negate());
	}

	@Override
	public Matrix<F> times(F a) {
		return new SingleValueVectorDiagonalMatrix<F>(size, a);
	}

	@Override
	public Matrix<F> adjoint() {
		return new SingleValueVectorDiagonalMatrix<F>(size,value.times(value));
	}
	
	@Override
	public Matrix<F> inverse() {
		F det = this.determinant();
		
		if (det.equals(zero)){
			throw new ArithmeticException("Inverse matrix does not exist");
		}
		
		if (value.times(value).equals(value)){
			// value is ONE
			return this;
		} else {
			return new SingleValueVectorDiagonalMatrix<F>(size,value.times(value).over(this.determinant()));
		}
	}
	
	@Override
	public F cofactor(int r, int c) {
		return r==c ? value.times(value).over(this.determinant()) : zero;
	}


	@Override
	public Vector<F> getDiagonal() {
		return DenseVector.vector(size,value);
	}

	public boolean isSquare(){
		return true;
	}

	@Override
	public boolean hasInverse() {
		return !this.determinant().equals(zero);
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
			return new SingleValueVectorDiagonalMatrix<F>(size,value.plus(((SingleValueVectorDiagonalMatrix<F>)other).value));
		} else {
			return other.plus(this);
		}
	}






}

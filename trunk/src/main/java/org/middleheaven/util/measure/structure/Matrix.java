package org.middleheaven.util.measure.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.middleheaven.util.measure.Real;


public abstract class Matrix<F extends Field<F>> implements VectorSpace<Matrix<F>,F>, Ring<Matrix<F>>{


	public static Matrix<Real> random(int rows, int columns){
		return random(rows,columns,2);
	}
	
	public static Matrix<Real> random(int rows, int columns , long seed){
		return random(rows,columns,new Random(seed));
	}
	
	public static Matrix<Real> random(int rows, int columns , Random r){
		List<DenseVector<Real>> all = new ArrayList<DenseVector<Real>>(rows);
		List<Real> elements = new ArrayList<Real>(columns);
		int counts =0;
		int length = rows*columns;
		for (int i =0; i < length; i++, counts++){
			if ( counts / columns == 1){
				counts=0;
				all.add((DenseVector<Real>)DenseVector.vector(elements));
				elements = new ArrayList<Real>(columns);
			} 
			elements.add(Real.valueOf(r.nextDouble()));
		}
		all.add((DenseVector<Real>)DenseVector.vector(elements));
		return new DenseMatrix<Real>(all);
	}
	
	public static Matrix<Real> identity(int size) {
		return new SingleValueVectorDiagonalMatrix<Real>(size, Real.ONE());
	}
	
	public static <F extends Field<F>> Matrix<F> identity(int size, F one) {
		return new SingleValueVectorDiagonalMatrix<F>(size, one);
	}
	
	public static <T extends Field<T>> Matrix<T> matrix(Vector<T>... rows){
		List<DenseVector<T>> all = new ArrayList<DenseVector<T>>();
		for (Vector<T> v : rows){
			all.add((DenseVector<T>)DenseVector.vector(v));
		}
		return new DenseMatrix<T>(all);
	}

	public static <T extends Field<T>> Matrix<T> matrix(int rows, int columns, T ... values){
		if (values.length != rows * columns){
			throw new IllegalArgumentException("Invalid dimentions");
		}
		
		List<DenseVector<T>> all = new ArrayList<DenseVector<T>>(rows);
		List<T> elements = new ArrayList<T>(columns);
		int counts =0;
		for (int i =0; i < values.length; i++, counts++){
			if ( counts / columns == 1){
				counts=0;
				all.add((DenseVector<T>)DenseVector.vector(elements));
				elements = new ArrayList<T>(columns);
			} 
			elements.add(values[i]);
		}
		all.add((DenseVector<T>)DenseVector.vector(elements));
		return new DenseMatrix<T>(all);
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
		
		return this.equals(this.transpose());
	}
	
	
	@Override
	public final Matrix<F> minus(Matrix<F> other) {
		return this.plus(other.negate());
	}

	
	public abstract Matrix<F> adjoint();

	public abstract F determinant();

	public abstract Vector<F> getRow(int row);

	public abstract Vector<F> getColumn(int column);

	public abstract Vector<F> getDiagonal();

	public abstract Vector<F> times (Vector<F> vector);

	public abstract Matrix<F> transpose();
	
	public final F trace(){
		F result = this.get(0, 0);
		for (int i=1; i<this.rowsCount();i++){
			result = result.plus(this.get(i,i));
		}
		return result;
	}

	public abstract F get(int r, int c);
	public abstract int rowsCount();
	public abstract int columnsCount();
	public abstract F cofactor(int r, int c);

	public abstract boolean hasInverse();
	public abstract Matrix<F> inverse();
	
	@SuppressWarnings("unchecked")
	public boolean equals(Object other){
		return other instanceof Matrix && equals((Matrix<F>)other); 
	}
	
	public boolean equals(Matrix<?> other){
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

	@Override
	public Matrix<F> one() {
		return identity(this.columnsCount(), this.get(0, 0).one());
	}
	
	@Override
	public Matrix<F> zero() {
		return identity(this.columnsCount(), this.get(0, 0).zero());
	}
}

/**
 * 
 */
package org.middleheaven.quantity.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.middleheaven.quantity.math.structure.Field;

/**
 * 
 */
public class DenseVectorSpaceProvider implements VectorSpaceProvider {

	private static final long serialVersionUID = 7559322914362799713L;

	public Matrix<Real> random(int rows, int columns){
		return random(rows,columns,2);
	}

	public Matrix<Real> random(int rows, int columns , long seed){
		return random(rows,columns,new Random(seed));
	}

	public Matrix<Real> random(int rows, int columns , Random r){

		int length = rows*columns;

		DenseMatrix<Real> result = new DenseMatrix<Real>(rows, columns);

		for (int i=0; i < length; i++){
			result.setByIndex(i, Real.valueOf(r.nextDouble()));
		}

		return result;

	}

	public Matrix<Real> identity(int size) {
		return diagonal(size,Real.ONE());
	}

	public <T extends Field<T>> Matrix<T> diagonal(int size, final T value) {
		
		return new ProxyMatrix<T>(this, size, size){

			@Override
			public T get(int r, int c) {
				return r == c ? value : value.zero();
			}
			
		};
	}


	public <T extends Field<T>> Matrix<T> matrix(Vector<T>... rows){

		if (rows.length == 0){
			throw new IllegalArgumentException("rows cannot be empty");
		}

		int columns = rows[0].size();

		DenseMatrix<T> result = new DenseMatrix<T>(rows.length, columns);

		for (int r=0; r < rows.length; r++){

			Vector<T> vector = rows[r];

			if (vector.size() != columns){
				throw new IllegalArgumentException("rows do not have the same size");
			}

			for (int c = 0; c < columns; c++){
				result.set(r,c , vector.get(c));
			}
		}


		return result;

	}

	public <T extends Field<T>> Matrix<T> matrix(int rows, int columns, T ... values){
		if (values.length != rows * columns){
			throw new IllegalArgumentException("Invalid dimensions");
		}

		DenseMatrix<T> result = new DenseMatrix<T>(rows, columns);

		for (int i=0; i < values.length; i++){
			result.setByIndex(i, values[i]);
		}


		return result;

	}

	public <T extends Field<T>> Vector<T> vectorize(List<T> elements) {
		return new DenseVector<T>(elements);
	}

	public <T extends Field<T>> Vector<T> vectorize(Vector<T> other) {
		return new DenseVector<T>(other);
	}

	public <T extends Field<T>> Vector<T> vectorize(int dimension, T value) {
		return new DenseVector<T>(dimension,value);
	}

	public Vector<Real> vector(java.lang.Number ... elements){

		Real[] relements = new Real[elements.length];
		for (int i =0;i < elements.length;i++){
			relements[i] = Real.valueOf(elements[i]);
		}

		return  vector(Arrays.asList(relements));
	}

	public  <T extends Field<T>> Vector<T> vector(T ... elements){
		return vector(Arrays.asList(elements));
	}

	public <T extends Field<T>> Vector<T> vector(List<T> elements){
		return vectorize(elements);
	}

	public <T extends Field<T>> Vector<T> vector(int size, T element){
		List<T> elements = new ArrayList<T>(size);
		for (int i=0;  i< size; i++){
			elements.add(element);
		}
		return vector(elements);
	}

	public  <T extends Field<T>> Vector<T> vector(Vector<T> other){
		return vectorize(other);
	}

	public  <T extends Field<T>> Vector<T> replicate(int dimension, T value){
		return vectorize(dimension, value);
	}

}

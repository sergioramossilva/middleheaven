/**
 * 
 */
package org.middleheaven.quantity.math;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import org.middleheaven.quantity.math.structure.Field;

/**
 * 
 */
public interface VectorSpaceProvider extends Serializable{
	

	public Matrix<Real> random(int rows, int columns);
	
	public Matrix<Real> random(int rows, int columns , long seed);
	
	public Matrix<Real> random(int rows, int columns , Random r);
	
	public Matrix<Real> identity(int size);
	
	public <F extends Field<F>> Matrix<F> diagonal(int size, F value);
	
	public <T extends Field<T>> Matrix<T> matrix(Vector<T>... rows);

	public <T extends Field<T>> Matrix<T> matrix(int rows, int columns, T ... values);
	
	public <T extends Field<T>> Vector<T> vectorize(List<T> elements);

	public <T extends Field<T>> Vector<T> vectorize(Vector<T> other);

	public <T extends Field<T>> Vector<T> vectorize(int dimension, T value);
	
	
	public Vector<Real> vector(java.lang.Number ... elements);
	
	public <T extends Field<T>> Vector<T> vector(T ... elements);
	
	public<T extends Field<T>> Vector<T> vector(List<T> elements);
	
	public <T extends Field<T>> Vector<T> vector(int size, T element);
	
	public <T extends Field<T>> Vector<T> vector(Vector<T> other);

	public  <T extends Field<T>> Vector<T> replicate(int dimension, T value);


}

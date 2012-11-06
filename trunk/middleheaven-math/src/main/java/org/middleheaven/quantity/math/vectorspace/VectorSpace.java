package org.middleheaven.quantity.math.vectorspace;

import java.util.List;
import java.util.Random;

import org.middleheaven.quantity.math.structure.Field;
import org.middleheaven.quantity.math.structure.FieldElement;

/**
 * A finite vector space of elements V  over a field F with a given dimension.
 * 
 * @param <V> the Vector Space element.
 * @param <F> the underlying field element
 */
public interface VectorSpace<V,F extends FieldElement<F>> {

	public F dotProduct();


	public Vector<F> vector(List<F> elements);

	/**
	 * Creates a Vector with the value in all elements.
	 * @param value
	 * @return
	 */
	public Vector<F> replicate(F value);

	public Vector<F> vector(F ... elements);

	public Vector<F> vector(Number ... elements);
	
	/**
	 * @return
	 */
	public Field<F> getField();
	
	/**
	 * 
	 * @return
	 */
	public int getDimensions();
	
	public Matrix<F> random(int rows, int columns);
	
	public Matrix<F> random(int rows, int columns , long seed);
	
	public Matrix<F> random(int rows, int columns , Random r);
	
	public Matrix<F> identity(int size);
	
	/**
	 * Creates a square size rows x size columns matrix with the given value on the diagonal.
	 * @param rows
	 * @param columns
	 * @param value diagonal value
	 * @return
	 */
	public Matrix<F> diagonal(int size , F value);
	
	public Matrix<F> matrix(Vector<F>... rows);

	public  Matrix<F> matrix(int rows, int columns, F ... values);
	
	

	
}

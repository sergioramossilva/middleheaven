/**
 * 
 */
package org.middleheaven.quantity.math.vectorspace;

import org.middleheaven.quantity.math.Conjugatable;
import org.middleheaven.quantity.math.UnivariateFunction;
import org.middleheaven.quantity.math.structure.Field;
import org.middleheaven.quantity.math.structure.FieldElement;
import org.middleheaven.quantity.math.structure.RingElement;

/**
 * 
 */
public interface Matrix<F extends FieldElement<F>> extends RingElement<Matrix<F>>, Conjugatable<Matrix<F>>{


	/**
	 * @return the underlying field being used for the elements of the matrix.
	 */
	public Field<F> getField();

	/**
	 * 
	 * @return <code>true</code> if this matrix is square, i.e. the number of rows equals the number of columns.
	 */
	public boolean isSquare();
	
	/**
	 * 
	 * @return <code>true</code> if matrix.get(i,j).equals(matrix.get(j,i) for all i and j.
	 */
	public boolean isSimmetric();
	
	public Matrix<F> times(F scalar);
	
	/**
	 * Obtain the value at position (r,c)
	 * @param r the row index. 0 is the first.
	 * @param c the column index. 0 is the first.
	 * @return
	 */
	public abstract F get(int r, int c);
	
	/**
	 * sets the value at position (r,c)
	 * @param r
	 * @param c
	 * @param value
	 * @return
	 */
	//public abstract Matrix<F> set(int r, int c, F value);
	
	/**
	 * 
	 * @return the number of rows
	 */
	public abstract int rowsCount();
	
	/**
	 * 
	 * @return the number of columns
	 */
	public abstract int columnsCount();
	
	/**
	 * 
	 * @param r
	 * @param c
	 * @return
	 */
	public abstract F cofactor(int r, int c);
	
	/**
	 * 
	 * @return <code>true</code> if this matrix has an inverse.
	 */
	public abstract boolean hasInverse();

	/**
	 * 
	 * @return the inverse matrix.
	 * @throws ArithmeticException if no inverse exists.
	 */
	public Matrix<F> inverse();
	
	/**
	 * 
	 * @return this matrix adjoint matrix
	 */
	public abstract Matrix<F> adjoint();

	/**
	 * 
	 * @return this matrix determinant
	 */
	public abstract F determinant();

	/**
	 * Retrives a row as a vector.
	 * @param row the index of the row to retrive.
	 * @return a Vector<F> representing the values in the <code>row</code> row.
	 */
	public abstract Vector<F> getRow(int row);

	/**
	 * Retrives a column as a vector.
	 * @param column the index of the column to retrive.
	 * @return a Vector<F> representing the values in the <code>column</code> column.
	 */
	public abstract Vector<F> getColumn(int column);

	public abstract Vector<F> getDiagonal();

	/**
	 * Calculates y[i] = A[i,j] * x[j];
	 * 
	 * @param vector
	 * @return
	 */
	public abstract Vector<F> rightTimes (Vector<F> vector);

	
	/**
	 * Calculates y[i] =  x[j] * A[j,i];
	 * 
	 * @param vector
	 * @return
	 */
	public abstract Vector<F> leftTimes (Vector<F> vector);

	
	/**
	 * 
	 * @return this matrix transpose.
	 */
	public Matrix<F> transpose();
	
	/**
	 * 
	 * @return this matrix trace
	 */
	public F trace();


	/**
	 * Duplicates this matrix
	 * @return
	 */
	//public Matrix<F> duplicate();
	

	/**
	 * Return a matrix where the r row and the c column have bean removed
	 * @param row index of the row to remove
	 * @param column index of the column to remove.
	 * @return
	 */
	public Matrix<F> remove(final int row, final int column);
	
	/**
	 * Creates a new matrix where the values are com
	 * @param classifier
	 * @return
	 */
	public <N extends FieldElement<N>> Matrix<N> apply(UnivariateFunction<F, N> function);



}

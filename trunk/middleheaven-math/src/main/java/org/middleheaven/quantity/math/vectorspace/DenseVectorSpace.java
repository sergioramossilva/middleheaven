/**
 * 
 */
package org.middleheaven.quantity.math.vectorspace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.middleheaven.quantity.math.structure.Field;
import org.middleheaven.quantity.math.structure.FieldElement;

/**
 * 
 */
public class DenseVectorSpace<F extends FieldElement<F>> implements VectorSpace<Vector<F>, F> {


	private Field<F> field;
	private int dimensions;

	DenseVectorSpace (int dimensions, Field<F> field){
		this.field = field;
		this.dimensions = dimensions;
	}

	public  Vector<F> replicate(F value) {
		return new DenseVector<F>(this , value);
	}

	public Vector<F> vector(F ... elements){
		return vector(Arrays.asList(elements));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector<F> vector(Number... elements) {
		
		List<F> values = new ArrayList<F>();
		
		for (Number  n : elements){
			values.add(field.fromNumber(n));
		}
		
		return this.vector(values);
	}


	public Vector<F> vector(List<F> elements){
		
		if (elements.size() != this.dimensions) {
			throw new IllegalArgumentException("Dimensions not match.Expected " + dimensions + " sean " +elements.size());
		}
		return new DenseVector<F>(this , elements);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public F dotProduct() {
		throw new UnsupportedOperationException("dot Product is not supported in this space");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<F> getField() {
		return field;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDimensions() {
		return this.dimensions;
	}

	public Matrix<F> random(int rows, int columns){
		return random(rows,columns,2);
	}

	public Matrix<F> random(int rows, int columns , long seed){
		return random(rows,columns,new Random(seed));
	}

	public Matrix<F> random(int rows, int columns , Random r){

		int length = rows*columns;

		DenseMatrix<F> result = new DenseMatrix<F>(rows, columns, field.one());

		for (int i=0; i < length; i++){
			result.setByIndex(i, field.random(r));
		}

		return result;

	}

	public Matrix<F> identity(int size) {
		return diagonal(size, field.one());
	}

	public Matrix<F> diagonal(int size, final F value) {
		
		return new ProxyMatrix<F>(this, size, size){

			@Override
			public F get(int r, int c) {
				return r == c ? value : value.getAlgebricStructure().zero();
			}
			
		};
	}


	public  Matrix<F> matrix(Vector<F>... rows){

		if (rows.length == 0){
			throw new IllegalArgumentException("rows cannot be empty");
		}

		int columns = rows[0].size();

		DenseMatrix<F> result = new DenseMatrix<F>(rows.length, columns, field.zero());

		for (int r=0; r < rows.length; r++){

			Vector<F> vector = rows[r];

			if (vector.size() != columns){
				throw new IllegalArgumentException("rows do not have the same size");
			}

			for (int c = 0; c < columns; c++){
				result.set(r,c , vector.get(c));
			}
		}


		return result;

	}

	public Matrix<F> matrix(int rows, int columns, F ... values){
		if (values.length != rows * columns){
			throw new IllegalArgumentException("Invalid dimensions");
		}

		DenseMatrix<F> result = new DenseMatrix<F>(rows, columns, field.zero());

		for (int i=0; i < values.length; i++){
			result.setByIndex(i, values[i]);
		}


		return result;

	}

	

}

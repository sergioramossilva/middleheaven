package org.middleheaven.quantity.structure;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class DiagonalMatrix<F extends Field<F>> extends Matrix<F> {


	@Override
	public Matrix<F> times(Matrix<F> other) {
		int n = this.rowsCount();
		Vector<F>[] vectors = new Vector[n];
		for (int i =0; i < n; i++){
			vectors[i] = other.getRow(i).times(this.get(i, i));
		}
		return DenseMatrix.matrix(vectors);
	}
	
	public Vector<F> getVector(int index) {
		Object[] v = new Object[this.rowsCount()];
		Arrays.fill(v, get(index,index).zero());
		v[index]= get(index,index);
		return DenseVector.vector( new ArrayList(Arrays.asList(v)));
	}
	
	@Override
	public final Vector<F> getColumn(int column) {
		return getVector(column);
	}

	@Override
	public final Vector<F> getRow(int row) {
		return getVector(row);
	}
	
}

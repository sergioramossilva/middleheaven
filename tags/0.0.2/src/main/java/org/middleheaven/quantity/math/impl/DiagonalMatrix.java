package org.middleheaven.quantity.math.impl;

import java.util.ArrayList;
import java.util.Arrays;

import org.middleheaven.quantity.math.Matrix;
import org.middleheaven.quantity.math.Vector;
import org.middleheaven.quantity.math.structure.Field;

abstract class DiagonalMatrix<F extends Field<F>> extends Matrix<F> {


	
	@Override
	public Matrix<F> times(Matrix<F> other) {
		int n = this.rowsCount();
		@SuppressWarnings("unchecked") Vector<F>[] vectors = new Vector[n];
		for (int i =0; i < n; i++){
			vectors[i] = other.getRow(i).times(this.get(i, i));
		}
		return DenseMatrix.matrix(vectors);
	}
	
	public Vector<F> getVector(int index) {
		Object[] v = new Object[this.rowsCount()];
		Arrays.fill(v, get(index,index).zero());
		v[index]= get(index,index);
		
		@SuppressWarnings("unchecked") Vector<F> result = DenseVector.vector( new ArrayList(Arrays.asList(v)));
		
		return result;
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

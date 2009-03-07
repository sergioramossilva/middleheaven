package org.middleheaven.quantity.structure;

public interface MatrixInvertionAlgorithm {

	public <F extends Field<F>> Matrix<F> invert( Matrix<F> matrix);
}

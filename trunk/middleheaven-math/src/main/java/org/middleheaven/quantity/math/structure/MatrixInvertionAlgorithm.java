package org.middleheaven.quantity.math.structure;

import org.middleheaven.quantity.math.vectorspace.Matrix;

public interface MatrixInvertionAlgorithm {

	public <F extends FieldElement<F>> Matrix<F> invert( Matrix<F> matrix);
}

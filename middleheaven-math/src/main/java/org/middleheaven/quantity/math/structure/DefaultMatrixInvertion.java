package org.middleheaven.quantity.math.structure;

import org.middleheaven.quantity.math.Matrix;

public class DefaultMatrixInvertion implements MatrixInvertionAlgorithm{

	@Override
	public <F extends Field<F>> Matrix<F> invert(Matrix<F> matrix) {
		
		F zero = matrix.get(0, 0).zero();
		
		F det = matrix.determinant();
		
		if (!(matrix.isSquare() && !det.equals(zero))){
			throw new ArithmeticException("Inverse matrix does not exist");
		}

		return matrix.adjoint().times(det.inverse()).transpose();
		
	}

}

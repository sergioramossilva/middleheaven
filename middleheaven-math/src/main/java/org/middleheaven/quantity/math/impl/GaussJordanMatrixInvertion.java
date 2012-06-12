package org.middleheaven.quantity.math.impl;

import org.middleheaven.quantity.math.DenseMatrix;
import org.middleheaven.quantity.math.Matrix;
import org.middleheaven.quantity.math.structure.Field;
import org.middleheaven.quantity.math.structure.MatrixInvertionAlgorithm;

public class GaussJordanMatrixInvertion implements MatrixInvertionAlgorithm{


	public <F extends Field<F>> Matrix<F> invert( Matrix<F> matrix){

		if (!matrix.isSquare()){
			throw new ArithmeticException("Matrix not invertable");
		}
		final F ZERO = matrix.get(0, 0).zero();
		final F ONE = matrix.get(0, 0).one();
	
		Matrix<F> A = DenseMatrix.augmentWithEntity(matrix);


		int i = 0;
		int j = 0;
		int k;
		final int n = A.rowsCount();
		final int m = A.columnsCount();
		
		while( i<n && j<m ){

			//look for a non-zero entry in col j at or below row i
			k = i;
			while( k<n && A.get(k,j).equals(ZERO) ){
				k++;
			}

			// if such an entry is found at row k
			if( k<=n  ){

				//  if k is not i, then swap row i with row k
				if( k!=i ) {
					swap(A, i, k, j);
				}

				// if A[i][j] is not 1, then divide row i by A[i][j]
				if( !A.get(i,j).equals(ONE) ){
					divide(A, i, j, ONE);
				}

				// eliminate all other non-zero entries from col j by subtracting from each
				// row (other than i) an appropriate multiple of row i
				eliminate(A, i, j,ZERO);
				i++;
			}
			j++;
		}

		Matrix<F> I =  new DenseMatrix<F>(A,0 , A.columnsCount()/2,3,3);
		
		return I;
	}

	// swap()
	// swap row i with row k
	// pre: A[i][q]==A[k][q]==0 for 1<=q<j
	private  static  <F extends Field<F>>  void swap(Matrix<F> A, int i, int k, int j){
		final int m = A.columnsCount();
		for(int q=j - 1; q<m; q++){
			F temp = A.get(i,q);
			A.set(i,q,A.get(k,q));
			A.set(k,q,temp);
		}
	}

	// divide()
	// divide row i by A[i][j]
	// pre: A[i][j]!=0, A[i][q]==0 for 1<=q<j
	// post: A[i][j]==1;
	private static <F extends Field<F>>  void divide(Matrix<F> A, int i, int j,F ONE){
		final int m = A.columnsCount() - 1;
		for(int q=j+1; q<=m; q++){
			A.set(i, q , A.get(i,q).over( A.get(i,j)));
		}
		A.set(i,j ,ONE);
	}

	// eliminate()
	// subtract an appropriate multiple of row i from every other row
	// pre: A[i][j]==1, A[i][q]==0 for 1<=q<j
	// post: A[p][j]==0 for p!=i
	private static <F extends Field<F>>  void eliminate(Matrix<F> A, int i, int j, F ZERO){
		final int n = A.rowsCount() - 1;
		final int m = A.columnsCount() - 1;
		for(int p=0; p<=n; p++){
			if( p!=i && !A.get(p, j).equals(ZERO) ){
				for(int q=j+1; q<=m; q++){
					A.set(p,q , A.get(p, q).minus (A.get(p,j).times(A.get(i,q))));
				}
				A.set(p,j, ZERO);
			}
		}
	}
}

package org.middleheaven.quantity.math.impl;

import org.middleheaven.quantity.math.Matrix;
import org.middleheaven.quantity.math.structure.Field;


/** LU Decomposition.
<P>
For an m-by-n matrix A with m >= n, the LU decomposition is an m-by-n
unit lower triangular matrix L, an n-by-n upper triangular matrix U,
and a permutation vector piv of length m so that A(piv,:) = L*U.
If m < n, then L is m-by-m and U is m-by-n.
<P>
The LU decomposition with pivoting always exists, even if the matrix is
singular, so the constructor will never fail.  The primary use of the
LU decomposition is in the solution of square systems of simultaneous
linear equations.  This will fail if isNonsingular() returns false.
 */
public class LUDecomposition<F extends Field<F>> {


	private EditableMatrix<F> LU;

	/** Row and column dimensions, and pivot sign.
	   @serial column dimension.
	   @serial row dimension.
	   @serial pivot sign.
	 */
	
	private F pivsign; 

	/** Internal storage of pivot vector.
	   @serial pivot vector.
	 */
	private int[] piv;

	final F ZERO;
	final F ONE;

	/* ------------------------
	   Constructor
	 * ------------------------ */

	/** LU Decomposition
	   @param  A   Rectangular matrix
	   @return     Structure to access L, U and piv.
	 */

	private F abs(F value){
		if (value instanceof Comparable){
			if (((Comparable<F>)value).compareTo(ZERO)>=0){
				return value;
			}
			return value.negate();
		}
		throw new IllegalArgumentException ("Field is not ordable");
	}

	private int compare(F a, F b){
		if (a instanceof Comparable){
			return ((Comparable<F>)a).compareTo(b);
		}
		throw new IllegalArgumentException ("Field is not ordable");
	}
	
	public LUDecomposition (Matrix<F> M) {

		// Use a "left-looking", dot-product, Crout/Doolittle algorithm.

		ZERO = M.get(0, 0).zero();
		ONE = M.get(0, 0).one();
		
		if (!(ONE instanceof Comparable)){
			throw new IllegalArgumentException ("Field is not ordable");
		}

		LU = new EditableMatrix<F>(M);
		piv = new int[LU.rowsCount()];
		
		for (int i = 0; i < LU.rowsCount(); i++) {
			piv[i] = i;
		}
		
		pivsign = ONE;
		
		EditableVector<F> LUcolj;
		EditableVector<F> LUrowi;
		

		// Outer loop.

		for (int j = 0; j < LU.columnsCount(); j++) {

			// Make a copy of the j-th column to localize references.

			LUcolj = new EditableVector<F>(LU.getColumn(j));


			// Apply previous transformations.

			for (int i = 0; i < LU.rowsCount(); i++) {
				LUrowi = new EditableVector<F>(LU.getRow(i));

				// Most of the time is spent in the following dot product.

				int kmax = Math.min(i,j);
				F s = ZERO;
				for (int k = 0; k < kmax; k++) {
					s = s.plus(LUrowi.get(k).times(LUcolj.get(k)));
				}
				F  v = LUcolj.get(i).minus(s);
				LUcolj.set(i, v);
				LUrowi.set(j , v);
				
			}

			// Find pivot and exchange if necessary.

			int p = j;
			for (int i = j+1; i < LU.rowsCount(); i++) {
				if (compare(abs(LUcolj.get(i)), abs(LUcolj.get(p)))>0) {
					p = i;
				}
			}
			if (p != j) {
				for (int k = 0; k < LU.columnsCount(); k++) {
					F temp = LU.get(p,k); 
					LU.set(p,k,  LU.get(j,k)); 
					LU.set(j,k, temp );
				}
				int k = piv[p]; 
				piv[p] = piv[j]; 
				piv[j] = k;
				pivsign = pivsign.negate();
			}

			// Compute multipliers.

			if (j < LU.rowsCount() && !LU.get(j,j).equals(ZERO)) {
				for (int i = j+1; i < LU.rowsCount(); i++) {
					LU.set(i,j , LU.get(i,j).over(LU.get(j,j)));
				}
			}
		}
	}


	/* ------------------------
	   Public Methods
	 * ------------------------ */

	/** Is the matrix nonsingular?
	   @return     true if U, and hence A, is nonsingular.
	 */

	public boolean isNonsingular () {
		for (int j = 0; j < LU.columnsCount(); j++) {
			if (LU.get(j, j).equals(ZERO)){
				return false;
			}
		}
		return true;
	}

	/** Return lower triangular factor
	   @return     L
	 */

	public Matrix<F> getL () {
		EditableMatrix<F> L = new EditableMatrix<F>(LU.rowsCount(),LU.columnsCount(),ZERO);
		for (int i = 0; i < LU.rowsCount(); i++) {
			for (int j = 0; j < LU.columnsCount(); j++) {
				if (i > j) {
					L.set(i,j , LU.get(i,j));
				} else if (i == j) {
					L.set(i,j ,ONE);
				} else {
					L.set(i,j ,ZERO);
				}
			}
		}
		return L;
	}

	/** Return upper triangular factor
	   @return     U
	 */

	public Matrix<F> getU () {
		EditableMatrix<F> U = new EditableMatrix<F>(LU.columnsCount(),LU.columnsCount(),ZERO);
		for (int i = 0; i < LU.columnsCount(); i++) {
			for (int j = 0; j < LU.columnsCount(); j++) {
				if (i <= j) {
					U.set(i,j ,  LU.get(i,j));
				} else {
					U.set(i,j ,  ZERO);
				}
			}
		}
		return U;
	}

	/** Return pivot permutation vector
	   @return     piv
	 */

	public int[] getPivot () {
		int[] p = new int[LU.rowsCount()];
		for (int i = 0; i < LU.rowsCount(); i++) {
			p[i] = piv[i];
		}
		return p;
	}

	/** Return pivot permutation vector as a one-dimensional double array
	   @return     (double) piv
	 */

	public double[] getDoublePivot () {
		double[] vals = new double[LU.rowsCount()];
		for (int i = 0; i < LU.rowsCount(); i++) {
			vals[i] = (double) piv[i];
		}
		return vals;
	}

	/** Determinant
	   @return     det(A)
	   @exception  IllegalArgumentException  Matrix must be square
	 */

	public F det () {
		if (!LU.isSquare()) {
			throw new IllegalArgumentException("Matrix must be square.");
		}
		
		F d = pivsign;
		for (int j = 0; j < LU.columnsCount(); j++) {
			d = d.times(LU.get(j,j));
		}
		
		return d;
		
	}

	/** Solve A*X = B
	   @param  B   A Matrix with as many rows as A and any number of columns.
	   @return     X so that L*U*X = B(piv,:)
	   @exception  IllegalArgumentException Matrix row dimensions must agree.
	   @exception  RuntimeException  Matrix is singular.
	 */

	public Matrix<F> solve (Matrix<F> B) {
		if (B.rowsCount() != this.LU.rowsCount()) {
			throw new IllegalArgumentException("Matrix row dimensions must agree.");
		}
		if (!this.isNonsingular()) {
			throw new RuntimeException("Matrix is singular.");
		}

		// Copy right hand side with pivoting
		int nx = B.columnsCount();
		EditableMatrix<F> X = new EditableMatrix(B , piv,0,nx-1 );

		// Solve L*Y = B(piv,:)
		for (int k = 0; k < LU.columnsCount(); k++) {
			for (int i = k+1; i < LU.columnsCount(); i++) {
				for (int j = 0; j < nx; j++) {
					X.set(i,j, X.get(i, j).minus(X.get(k,j).times(LU.get(i,k))));
				}
			}
		}
		// Solve U*X = Y;
		for (int k = LU.columnsCount()-1; k >= 0; k--) {
			for (int j = 0; j < nx; j++) {
				X.set(k,j, X.get(k, j).over(LU.get(k,k)));
			}
			for (int i = 0; i < k; i++) {
				for (int j = 0; j < nx; j++) {
					X.set(i,j, X.get(i, j).minus(X.get(k,j).times(LU.get(i,k))));
				}
			}
		}
		return X;
	}
}



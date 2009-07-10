package org.middleheaven.test.math.structures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.quantity.math.BigInt;
import org.middleheaven.quantity.math.Matrix;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.math.Vector;
import org.middleheaven.quantity.math.impl.LUDecomposition;

public class StructuresTest {

	@Test
	public void testNumbers(){

		Real a = Real.valueOf(1.2);
		Real b = Real.valueOf(1.2);
		Real c = Real.valueOf(2.4);

		assertEquals(c, a.plus(b));

		BigInt i = BigInt.valueOf(10);
		BigInt j = BigInt.valueOf(12);

		assertEquals(Real.valueOf(11.2), a.plus(i));
		assertEquals(j, i.times(a));
	}

	@Test
	public void matrix(){

		Vector<Real> v1 = Vector.vector(1,1,2);
		Vector<Real> v2 = Vector.vector(1,2,1);
		Vector<Real> v3 = Vector.vector(2,1,1);

		Matrix<Real> M = Matrix.matrix(3,3, Real.valueOf(
				1 ,1 , 2, 
				1 , 2 , 1 , 
				2 ,1 ,1
		));

		Vector<Real> v4 = Vector.vector(2,2,4);
		Vector<Real> v5 = Vector.vector(2,4,2);
		Vector<Real> v6 = Vector.vector(4,2,2);

		Matrix<Real> N = Matrix.matrix(3,3, Real.valueOf(2 , 2 , 4, 2 ,4 ,2,4, 2, 2));

		// determinant
		Real det = M.determinant();

		assertEquals(Real.valueOf(-4),det);

		assertEquals(Real.valueOf(4),M.trace());

		// transpose 
		assertEquals(M , M.transpose());

		// multiplication
		assertEquals(N , M.times(Real.valueOf(2.0)));

		// addition
		assertEquals(N , M.plus(M));

		// vector x matrix
		Vector<Real> v7 = Vector.vector(12,10,10);
		assertEquals(v7, M.times(v4));

		// Matrix equality
		Matrix<Real> P = Matrix.matrix(3,3, Real.valueOf(1 , 1 , 2, 1 ,2 ,1, 2, 1, 1));
		assertEquals(M, P);

		// Matrix multiplication
		Matrix<Real> Q = Matrix.matrix(3,3, Real.valueOf(12 , 10 , 10, 10 ,12 ,10, 10, 10, 12));
		assertEquals(Q, M.times(N));

		// Adjoint
		Matrix<Real> A = Matrix.matrix(3,3, Real.valueOf(1 , 1 , -3, 1 ,-3 ,1, -3, 1, 1));
		assertEquals(A, M.adjoint());

		// Identity Multiplication
		Matrix<Real> I = Matrix.identity(3);
		assertEquals(I, I.times(I));

		assertEquals(I.getRow(1), I.getColumn(1));

		Matrix<Real> X = Matrix.matrix(3,3, 
				Real.valueOf(
						1 ,  3 , 3, 
						1 , 4 ,3, 
						1, 3, 4
				));

		// Identity relation M  = M.I and M = I.M
		assertEquals(X, X.times(I));
		assertEquals(X, I.times(X));

		Matrix<Real> XInv = Matrix.matrix(3,3, Real.valueOf(
				7 ,  -3 , -3, 
				-1 , 1 ,0, 
				-1, 0, 1
		));

		// inverse
		assertEquals(XInv, X.inverse());

		// Invertion relation I = M.M^-1
		assertEquals(I, X.times(X.inverse()));

	}

	@Test(expected=ArithmeticException.class)
	public void matrixInversNotExists(){

		Matrix<Real> X = Matrix.matrix(3,3, Real.valueOf(
						1 ,  3 , 3, 
						1 , 3 ,3,  // two equal rows => zero determinant
						1, 3, 4
				));

		X.inverse();

	}

	@Test 
	public void randomMatrix (){
		Matrix<Real> R = Matrix.random(3,3,2);

		// same seed produces the same matrix
		assertEquals(R, Matrix.random(3,3,2));

		// the matrix is invertible
		assertTrue(R.hasInverse());
		
		Matrix<Real> I = Matrix.identity(3);
		
		assertEquals(I, R.times(R.inverse()));
		

	}


	@Test 
	public void matrixLU(){
		Matrix<Real> A = Matrix.matrix(3,3, Real.valueOf(
				6 , -2 , 0, 
				9 ,-1 ,1, 
				3, 7, 5
		));

		Matrix<Real> U = Matrix.matrix(3,3, Real.valueOf(
				1, 0.3 , 0, 
				0 ,1 ,0.5, 
				0, 0, 1
		));


		Matrix<Real> L = Matrix.matrix(3,3, Real.valueOf(
				6 , 0 , 0, 
				9 ,2 , 0, 
				3, -8, 1
		));

		LUDecomposition<Real> lud = new LUDecomposition<Real>(A);

		assertEquals(L , lud.getL());
		assertEquals(U , lud.getU());


		Matrix<Real> N = Matrix.matrix(3,3, Real.valueOf(1 , 1 , 2, 1 ,2 ,1,2, 1, 1));

		lud = new LUDecomposition<Real>(N);

		assertEquals(N , lud.getL().times(lud.getU()));
	}


}

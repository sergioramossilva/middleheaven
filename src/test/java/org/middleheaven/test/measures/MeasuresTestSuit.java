package org.middleheaven.test.measures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.util.measure.AngularPosition;
import org.middleheaven.util.measure.Complex;
import org.middleheaven.util.measure.DecimalMeasure;
import org.middleheaven.util.measure.Dimension;
import org.middleheaven.util.measure.IncompatibleDimentionException;
import org.middleheaven.util.measure.IncompatibleUnitsException;
import org.middleheaven.util.measure.Integer;
import org.middleheaven.util.measure.Real;
import org.middleheaven.util.measure.SI;
import org.middleheaven.util.measure.Unit;
import org.middleheaven.util.measure.measures.Distance;
import org.middleheaven.util.measure.measures.Energy;
import org.middleheaven.util.measure.measures.Force;
import org.middleheaven.util.measure.measures.Mass;
import org.middleheaven.util.measure.measures.Time;
import org.middleheaven.util.measure.measures.Velocity;
import org.middleheaven.util.measure.money.Money;
import org.middleheaven.util.measure.structure.Matrix;
import org.middleheaven.util.measure.structure.Vector;

public class MeasuresTestSuit {

	@Test
	public void testComplex(){
		
		Real r = Real.valueOf(-9);
		Complex rr = r.csqrt();
		assertEquals(Complex.valueOf(0,3),rr);
		
		r = Real.valueOf(9);
		rr = r.csqrt();
		assertEquals(Complex.valueOf(3,0),rr);
		
		Complex one = Complex.valueOf(1,0);
		
		assertEquals(Complex.ONE(),one);
		
		Complex i = Complex.valueOf(0,1);
		
		assertEquals(Complex.I(),i);
		
		assertEquals(one.negate(),i.times(i));
		
		Complex a = Complex.valueOf(4,2);
		Complex b = Complex.valueOf(9,3);
		Complex c = Complex.valueOf(13,5);
		Complex d = Complex.valueOf(30,30);
		Complex g = Complex.valueOf(Real.valueOf(3).inverse(),Real.valueOf(3).inverse());
		Complex v = Complex.valueOf(Real.valueOf(0.1),Real.valueOf(30).inverse());
		
		Complex x = b.inverse();
		
		assertEquals(c, a.plus(b));
		assertEquals(d, a.times(b));
		assertEquals(v, x);
		assertEquals(g, a.over(b));
		
		// conj(a+b) = conj(a) + conj(b)
		assertEquals(c.conjugate(), a.conjugate().plus(b.conjugate()));
		// conj(a*b) = conj(a) * conj(b)
		assertEquals(a.times(b).conjugate(), a.conjugate().times(b.conjugate()));
	}
	
	@Test
	public void testAngularPosition(){
		
		AngularPosition ap = AngularPosition.degrees(180);
		AngularPosition apc = ap.toRadians();
		
		assertEquals(AngularPosition.radians(Math.PI), apc );
		
		
		AngularPosition diff = AngularPosition.degrees(360*2.25);
		
		assertEquals(AngularPosition.degrees(270), ap.plus(diff).reduce());
		
		assertEquals(AngularPosition.degrees(90), ap.plus(diff.negate()).reduce());
	}
	
    @Test 
    public void matrixLU(){
    	
    	/*
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
    	
    	//assertEquals(L , lud.getL());
    	//assertEquals(U , lud.getU());
    	
    	
    	Matrix<Real> N = Matrix.matrix(3,3, Real.valueOf(1 , 1 , 2, 1 ,2 ,1,2, 1, 1));
		
    	lud = new LUDecomposition<Real>(N);
    	
    	assertEquals(N , lud.getL().times(lud.getU()));
    	*/
    }
    
	@Test
	public void matrix(){

		Vector<Real> v1 = Vector.vector(1,1,2);

		Matrix<Real> M = Matrix.matrix(3,3, Real.valueOf(1 , 1 , 2, 1 ,2 ,1, 2, 1, 1));

		Matrix<Real> N = Matrix.matrix(3,3, Real.valueOf(2 , 2 , 4, 2 ,4 ,2,4, 2, 2));
		
	
		Matrix<Real> Mi= Matrix.matrix(3,3, Real.valueOf(-0.25 , -0.25, -3*0.25 ,-0.25 ,  -3*0.25 ,-0.25, -3*0.25 , -0.25 , -0.25));
			
		Real det = M.determinant();
		
		assertEquals(Real.valueOf(-4),det);
		
		assertEquals(Real.valueOf(4),M.trace());
		
		assertEquals(M , M.transpose());
		
		assertEquals(N , M.times(Real.valueOf(2.0)));
		
		assertEquals(N , M.plus(M));
		
		assertEquals(Mi , M.inverse());
		
		Vector<Real> v7 = Vector.vector(12,10,10);
		assertEquals(v7, M.times(v1));
		
		Matrix<Real> P = Matrix.matrix(3,3, Real.valueOf(1 , 1 , 2, 1 ,2 ,1, 2, 1, 1));
		assertEquals(M, P);
		
		Matrix<Real> Q = Matrix.matrix(3,3, Real.valueOf(12 , 10 , 10, 10 ,12 ,10, 10, 10, 12));
		assertEquals(Q, M.times(N));
	
		Matrix<Real> A = Matrix.matrix(3,3, Real.valueOf(1 , 1 , -3, 1 ,-3 ,1, -3, 1, 1));
		assertEquals(A, M.adjoint());
		
		Matrix<Real> I = Matrix.identity(3);
		assertEquals(I, I.times(I));
		assertEquals(I, I.inverse());
		
		assertEquals(I.getRow(1), I.getColumn(1));
		assertEquals(I, M.times(M.inverse()));
		
		assertEquals(M, M.times(I));

	}

	@Test 
	public void randomMatrix (){
		Matrix<Real> R = Matrix.random(3,3,2);
	
		// same seed produces the same matrix
		assertEquals(R, Matrix.random(3,3,2));
		
		// the matrix is invertible
		assertTrue(R.hasInverse());
		assertEquals(Matrix.identity(3), R.times(R.inverse()));
		
		//big matrix
		Matrix<Real> P = Matrix.random(10,10,2);
		Matrix<Real> I = Matrix.identity(10);
		assertEquals(I, P.times(P.inverse()));
	}
	
	@Test
	public void testDimentions(){
		
		// create speed
		Dimension<Velocity> V = Dimension.LENGTH.over(Dimension.TIME);
		// assert right dimensions
		assertEquals("LT^-1", V.toString());
		assertEquals(Dimension.VELOCITY, V);
		// create acceleration
		Dimension<?> A = V.over(Dimension.TIME) ;
		// assert right dimensions
		assertEquals("LT^-2", A.toString());
		
		// create force
		Dimension<Force> F = A.times(Dimension.MASS) ;
		
		assertTrue(F.equals(A.times(Dimension.MASS)));
		assertFalse(F.equals(A));
		
		// get fundamental from calculus
		Dimension<Distance> L = V.times(Dimension.TIME);
		// assert is the same object
		assertSame(L , Dimension.LENGTH);
		
		try {
			L = L.plus(Dimension.LENGTH);
		} catch (IncompatibleDimentionException e){
			assertFalse (true);
		}

	}	
	
	@Test
	public void testUnits(){
		Unit<Distance> m = Unit.unit( Dimension.LENGTH, "m");
		Unit<Time> s = Unit.unit( Dimension.TIME, "s");
		
		m.plus(m);
		s.minus(s);
		
		Unit<Velocity> v = m.over(s);
		assertEquals("ms^-1" , v.symbol());
		
		
	    //m = v.times(s);
	    assertEquals("m" , m.symbol());
	    
	    // assert v not changed
	    assertEquals("ms^-1" , v.symbol());
		
	}	
	
	@Test
	public void testMeasures(){
		DecimalMeasure<Distance> L = DecimalMeasure.measure(3, 0.2 , SI.METER);
		DecimalMeasure<Distance> F = DecimalMeasure.measure(5, 0.1 , SI.METER);
		
		DecimalMeasure<Distance> S = F.plus(L);
		assertEquals (DecimalMeasure.measure(8, 0.3, SI.METER) , S);
		
		DecimalMeasure<Distance> D = F.times(L);
		assertEquals (DecimalMeasure.measure(15, 1.30, SI.METER.raise(2)) , D);
		
		
		Integer cem =  Integer.valueOf(100);
		Integer tres =  Integer.valueOf(3);
		Integer seis =  Integer.valueOf(6);
		Integer tresentos =  Integer.valueOf(300);
		
		assertEquals(seis,tres.plus(tres));
		
		assertEquals(tresentos,cem.times(tres));
		
		DecimalMeasure<Distance> l = DecimalMeasure.exact(200, SI.METER );
		DecimalMeasure<Time> t = DecimalMeasure.exact(10, SI.SECOND);
		DecimalMeasure<Distance> v = l.over(t);
		
		assertEquals(DecimalMeasure.exact(20,  SI.METER.over(SI.SECOND) ), v);
		assertEquals(Dimension.VELOCITY, v.unit().dimension());
		
		DecimalMeasure<Distance> v2 = v.times(v);
		Dimension<?> dim =  Dimension.VELOCITY.times(Dimension.VELOCITY);
		assertEquals(dim,v2.unit().dimension());
		DecimalMeasure<Mass> m = DecimalMeasure.exact(50, SI.KILOGRAM );
		DecimalMeasure<Energy> y = m.times(v2);
		DecimalMeasure<Energy> EC = y.times(Real.valueOf(0.5));
		
		assertEquals(Dimension.ENERGY, EC.unit().dimension());
		
	}
	
	@Test
	public void testMoney (){
		
		Money a = Money.money(100, "USD");
		Money b = Money.money(230, "USD");
		Money t = Money.money(330, "USD");
		
		Money c = Money.money(330, "EUR");
		
		Money m = a.plus(b);

		assertEquals(Money.class, m.getClass());
		assertEquals(t, m);
		assertFalse(t.equals(c));
		
		try {
			t.plus(c);
			assertFalse(true);
		} catch (IncompatibleUnitsException e){
			assertTrue(true);
		} catch (Exception e){
			assertFalse(true);
		}

		try {
			Money.money(330, "EU");
			assertFalse(true);
		} catch (IllegalArgumentException e){
			assertTrue(true);
		} catch (Exception e){
			assertFalse(true);
		}
		Real n = Real.valueOf(3);
		Money y = t.over(n);
		assertEquals (Money.money(110, "USD"), y);
		
		
	}
	
	@Test
	public void testNumbers(){
		
		Real a = Real.valueOf(1.2);
		Real b = Real.valueOf(1.2);
		Real c = Real.valueOf(2.4);
		
		assertEquals(c, a.plus(b));
		
		Integer i = Integer.valueOf(10);
		Integer j = Integer.valueOf(12);
		
		assertEquals(Real.valueOf(11.2), a.plus(i));
		assertEquals(j, i.times(a));
		
		
		assertEquals(Real.valueOf(1), Real.valueOf(1).over(3).times(3));
	}
	@Test
	public void testDurationAndPeriod(){
		
		/*
		Quantity<TimeInterval> days = Duration.days(30);
		Quantity<TimeInterval> months = Duration.months(1);
		Period p = new Period (2000);
		
		Quantity<TimeInterval> d = days.plus(months);
		
		assertEquals("1 months 30 days", d.toString());
		
		Quantity<TimeInterval> dp = d.minus(p);
		
		assertEquals("1 months 30 days -2000 miliseconds", dp.toString());
		
		*/
	}
	
	
}

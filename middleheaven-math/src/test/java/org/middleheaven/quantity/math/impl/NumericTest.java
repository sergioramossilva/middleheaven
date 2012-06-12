package org.middleheaven.quantity.math.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.middleheaven.quantity.math.Real;


public class NumericTest {

	@Test
	public void testSimplify(){
	

		BigDecimalReal real = (BigDecimalReal) BigDecimalReal.valueOf("3").over(BigDecimalReal.valueOf("15"));
	
		assertEquals(BigDecimalReal.valueOf("5") , real.inverse());
		
	}
	
	@Test
	public void testIsInteger(){
		assertTrue( BigDecimalMath.isInteger(BigDecimal.valueOf(84)));
		assertFalse( BigDecimalMath.isInteger(new BigDecimal("0.012")));
		assertFalse( BigDecimalMath.isInteger(new BigDecimal("1.2")));
	}
	
	@Test
	public void testLcm(){
		
		BigDecimal a = BigDecimal.valueOf(21 );
		BigDecimal b = BigDecimal.valueOf(6);
		BigDecimal c = BigDecimal.valueOf(42);

		assertEquals(c , BigDecimalMath.lcm(a, b));
		assertEquals(c , BigDecimalMath.lcm(b, a));
		assertEquals(c.negate() , BigDecimalMath.lcm(a.negate(), b.negate()));
		
		assertEquals(c , BigDecimalMath.lcm(a.negate(), b));
		

	}
	
	@Test
	public void testGcd(){
		BigDecimal a = BigDecimal.valueOf(84);
		BigDecimal b = BigDecimal.valueOf(18);
		BigDecimal c = BigDecimal.valueOf(6);
		
		BigDecimal k = BigDecimal.valueOf(13); 
		
		assertEquals(c , BigDecimalMath.gcd(a, b));
		assertEquals(c , BigDecimalMath.gcd(b, a));
		assertEquals(c.negate() , BigDecimalMath.gcd(a.negate(), b.negate()));
		
		assertEquals(c , BigDecimalMath.gcd(a.negate(), b));
		
		// cgd (a.k, b.k) = k .gcd(a,b)
		assertEquals(c.multiply(k) , BigDecimalMath.gcd(a.multiply(k), b.multiply(k)));
		
	}
	
	@Test
	public void testCos(){
		BigDecimal pi = BigDecimal.valueOf(Math.PI);
		final BigDecimal halfSqrt2 = new BigDecimal("0.707106781186547");
		
		int scale = 15;
		assertNumberEquals(BigDecimal.ONE,BigDecimalMath.cos(BigDecimal.ZERO, scale));
		assertNumberEquals(BigDecimal.ONE.negate(),BigDecimalMath.cos(pi, scale));
		assertNumberEquals(BigDecimal.ZERO,BigDecimalMath.cos(pi.divide(BigDecimal.valueOf(2)), scale));
		
		assertNumberEquals(halfSqrt2,BigDecimalMath.cos(pi.divide(BigDecimal.valueOf(4)), scale));
		
	}
	
	@Test
	public void testSin(){
		BigDecimal pi = BigDecimal.valueOf(Math.PI);
		final BigDecimal halfSqrt2 = new BigDecimal("0.707106781186547");
		
		int scale = 15;
		assertNumberEquals(BigDecimal.ZERO,BigDecimalMath.sin(BigDecimal.ZERO, scale));
		assertNumberEquals(BigDecimal.ZERO,BigDecimalMath.sin(pi, scale));
		assertNumberEquals(BigDecimal.ONE,BigDecimalMath.sin(pi.divide(BigDecimal.valueOf(2)), scale));
		assertNumberEquals(halfSqrt2,BigDecimalMath.sin(pi.divide(BigDecimal.valueOf(4)), scale));
		
	}
	
	@Test
	public void testArcTan(){
		
		assertEquals(Real.ZERO(),Real.ZERO().arctan());
		
		// arcTan(1/sqrt(3)) = pi/6
		
		assertEquals(Real.valueOf(Math.PI).over(6),Real.ONE().over(Real.valueOf(3).sqrt()).arctan());
		
		// arcTan(1) = pi/4
		
		Real pi4 = Real.valueOf(Math.PI).over(4);

		assertEquals(pi4,Real.ONE().arctan());
	}
	
	@Test
	public void testSinSQRT(){
		// sin(pi/4) = sqrt(2)/2
		
		Real pi4 = Real.valueOf(Math.PI).over(4);
		Real two = Real.valueOf(2);
		
		Real a = two.sqrt().over(2);
		Real b = pi4.sin();

		assertEquals(a,b);
	}

	@Test
	public void testCosSQRT(){
		// cos(pi/4) = sqrt(2)/2
		
		Real pi4 = Real.valueOf(Math.PI).over(4);
		Real two = Real.valueOf(2);
		
		Real a = two.sqrt().over(2);
		Real b = pi4.cos();

		assertEquals(a,b);
	}
	
	@Test
	public void testExp(){
		
		
		assertEquals(Real.valueOf(Math.E), Real.ONE().exp());
	}
	
	private void assertNumberEquals(BigDecimal a, BigDecimal b) {
		if(a.compareTo(b)!=0){
			throw new AssertionError("Expected <" + a + "> but was <" + b +">");
		}
	}
}

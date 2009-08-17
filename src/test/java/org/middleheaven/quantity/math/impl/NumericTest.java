package org.middleheaven.quantity.math.impl;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.middleheaven.quantity.math.Real;


public class NumericTest {

	
	@Test
	public void testCos(){
		BigDecimal pi = BigDecimal.valueOf(Math.PI);
		
		int scale = 15;
		assertNumberEquals(BigDecimal.ONE,BigDecimalMath.cos(BigDecimal.ZERO, scale));
		assertNumberEquals(BigDecimal.ONE.negate(),BigDecimalMath.cos(pi, scale));
		assertNumberEquals(BigDecimal.ZERO,BigDecimalMath.cos(pi.divide(BigDecimal.valueOf(2)), scale));
		assertNumberEquals(new BigDecimal("0.7071067811865476"),BigDecimalMath.cos(pi.divide(BigDecimal.valueOf(4)), scale));
		
	}
	
	@Test
	public void testSin(){
		BigDecimal pi = BigDecimal.valueOf(Math.PI);
		
		int scale = 15;
		assertNumberEquals(BigDecimal.ZERO,BigDecimalMath.sin(BigDecimal.ZERO, scale));
		assertNumberEquals(BigDecimal.ZERO,BigDecimalMath.sin(pi, scale));
		assertNumberEquals(BigDecimal.ONE,BigDecimalMath.sin(pi.divide(BigDecimal.valueOf(2)), scale));
		assertNumberEquals(new BigDecimal("0.707106781186548"),BigDecimalMath.sin(pi.divide(BigDecimal.valueOf(4)), scale));
		
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
	
	private void assertNumberEquals(BigDecimal a, BigDecimal b) {
		if(a.compareTo(b)!=0){
			throw new AssertionError("Expected <" + a + "> but was <" + b +">");
		}
	}
}

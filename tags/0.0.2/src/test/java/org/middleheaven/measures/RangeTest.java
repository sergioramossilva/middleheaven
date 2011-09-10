package org.middleheaven.measures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.CalendarDateIncrementor;
import org.middleheaven.quantity.time.Duration;
import org.middleheaven.util.collections.Range;

public class RangeTest {

	Range<Integer> R;
	Range<Integer> S;
	Range<Integer> E;
	Range<Integer> T;
	Range<Double> D;
	
	@Before 
	public void before(){
		R = Range.over(2,4);
		S = Range.over(7,10);
		T = Range.over(5,9);
		E = Range.emptyRange();
		D = Range.over(1.2, 4.8);
	}
	
	@Test
	public void testArgumentOrder() {
			
		try {
			Range.over(4,2);
			assertTrue(false);
		}catch (IllegalArgumentException e){
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testIsEmpty() {
		assertTrue(E.isEmpty());
	}
	
	@Test
	public void testIntersects() {
		assertTrue(S.intersects(T));
		assertTrue(T.intersects(S));
		assertFalse(S.intersects(R));
		assertFalse(S.intersects(E));
		
	}
	
	@Test
	public void testRaise() {
		assertEquals(Real.valueOf(1),Real.valueOf(2).raise(0));
		assertEquals(Real.valueOf(8),Real.valueOf(2).raise(3));
		assertEquals(Real.valueOf("0.1"),Real.valueOf(10).raise(-1));
	}
	
	@Test
	public void testIteration() {
		
		List<Integer> a = new ArrayList<Integer>();
		for (Integer i : R){
			a.add(i);
		}
		assertTrue(a.size()==3);
	
		List<Double> d = new ArrayList<Double>();
		
		for (Double i : Range.over(1.2, 4.1)){
			d.add(i);
		}
		assertTrue(d.size()==3);
		d.clear();
		
		// 0.1 is not correctly represented in double.
		for (Double i : Range.over(1.0, 1.6,0.1)){
			d.add(i);
		}
		assertFalse(7==d.size());
		
		
		// 0.1 is correctly represented in BigDecimal.
		List<BigDecimal> b = new ArrayList<BigDecimal>();
		Range<BigDecimal> range = Range.over(new BigDecimal("1.0"), new BigDecimal("1.6"),new BigDecimal("0.1"));
		
		for (BigDecimal i : range){
			b.add(i);
		}
		assertEquals(7,b.size());
		
		// the toList method does the same  
		
		assertEquals(7, range.toList().size());
		
		
		b = Range.over(new BigDecimal("1.0"), new BigDecimal("3.0")).toList();
		assertEquals(3,b.size());
	
		
		Range<?> rb = Range.over(new BigInteger("1"), new BigInteger("3"));
		assertEquals(3,rb.size());
		
		
		assertEquals(7, Range.over(Real.valueOf("1.0"), Real.valueOf("1.6"),Real.valueOf("0.1")).size());
		
		assertEquals(7, Real.valueOf("1.0").upTo(Real.valueOf("1.6"),Real.valueOf("0.1")).size());
		
		// Range over CalendarDate
		List <CalendarDate> list = CalendarDate.date(2007,1,5).upTo(CalendarDate.date(2007,1,10)).toList();
		assertEquals(6,list.size());

		assertEquals(6,Range.over(
				CalendarDate.date(2007,1,5), 
				CalendarDate.date(2007,1,10),
				new CalendarDateIncrementor(Duration.of().days(1))).size());
	}

	@Test
	public void testIntersect() {
		Range<Integer>  I = S.intersection(T);
		
		assertEquals(I, Range.over(7,9));
	
	}

	@Test
	public void testUnion() {
		Range<Integer>  U = S.union(T);
		assertEquals(U, Range.over(5,10));
	}

	@Test
	public void testContainsT() {
		assertTrue(S.contains(8));
		assertFalse(R.contains(8));
	}



}

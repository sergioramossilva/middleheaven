package org.middleheaven.measures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.time.CalendarDate;
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
	public void range(){
		
		// not empty
		Range<Integer> range = Range.over(1, 1);
		
		assertFalse("Range is empty" , range.isEmpty());

		assertEquals("Range is not 1 " , 1 , range.size());
		
		List<Integer> list = range.into(new ArrayList<Integer>());
		
		assertEquals(1, list.size());
		
		Range<Integer> emptyRange = Range.over(1, 1).excludeEnd(); // <=> [1, 1[ = []
		
		assertTrue("Range is not empty" , emptyRange.isEmpty());

		list = emptyRange.into(new ArrayList<Integer>());
		
		assertEquals(0, list.size());
		
		assertEquals("Range is not 0 " , 0 , emptyRange.size());
		
		emptyRange = Range.over(1, 1).excludeStart(); // <=> ]1, 1] = []
		
		assertTrue("Range is not empty" , emptyRange.isEmpty());

		assertEquals("Range is not 0 " , 0 , emptyRange.size());
		
	
		range = Range.over(1, 6);
		
		list = range.into(new ArrayList<Integer>());
		
		assertEquals(6, list.size());
		
		assertEquals(6, range.size());
		
		Set<Integer> selected = new HashSet<Integer>();
		for(int i=0;selected.size() != range.size() && i < 100000;i++){
			selected.add(range.random());
		}
		
		assertEquals(6,selected.size());
		
	}
	
	
	@Test
	public void testReverseOrder() {
			

			final Range<Integer> range = Range.over(4,2);
			
			
			assertTrue(range.contains(4));
			assertTrue(range.contains(3));
			assertTrue(range.contains(2));
			
			List<Integer> list = range.into(new ArrayList<Integer>());
			List<Integer> proof = Arrays.<Integer>asList(new Integer[]{4, 3, 2});
	
			assertEquals(proof, list);
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
		assertEquals(3, a.size());
	
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
		
		assertEquals(7, range.into(new ArrayList<BigDecimal>()).size());
		
		
		b = Range.over(new BigDecimal("1.0"), new BigDecimal("3.0")).into(new ArrayList<BigDecimal>());
		assertEquals(3,b.size());
	
		
		Range<?> rb = Range.over(new BigInteger("1"), new BigInteger("3"));
		assertEquals(3,rb.size());
		
		
		assertEquals(7, Range.over(Real.valueOf("1.0"), Real.valueOf("1.6"),Real.valueOf("0.1")).size());
		
		assertEquals(7, Real.valueOf("1.0").upTo(Real.valueOf("1.6"),Real.valueOf("0.1")).size());
		
		// Range over CalendarDate
		List <CalendarDate> list = CalendarDate.date(2007,1,5).upTo(CalendarDate.date(2007,1,10)).into(new ArrayList<CalendarDate>());
		assertEquals(6,list.size());

		assertEquals(6,Range.over(
				CalendarDate.date(2007,1,5), 
				CalendarDate.date(2007,1,10)
		).size());
	}

	@Test
	public void testIntersect() {
		Range<Integer>  I = S.intersection(T);
		
		assertEquals(I, Range.over(7,9));
	
	}

	@Test
	public void testContainsT() {
		assertTrue(S.contains(8));
		assertFalse(R.contains(8));
	}



}

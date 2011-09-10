package org.middleheaven.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.IteratorsIterator;


public class UtilsTest {


	@Test
	public void testIntersect (){
		
		Collection<String> a = new ArrayList<String>(); 
		a.add("A");
		a.add("A");
		a.add("B");
		a.add("C");
		a.add("D");
		
		Collection<String> b = new ArrayList<String>(); 
		b.add("A");
		b.add("B");
		b.add("B");
		b.add("D");
		
		Collection<String> r = new ArrayList<String>(); 
		r.add("A");
		r.add("B");
		r.add("D");
		
		assertTrue(CollectionUtils.equalContents(r, CollectionUtils.intersect(a, b)));
	}
	
	@Test
	public void testTime (){
		
		Collection<String> a = new ArrayList<String>(); 
		a.add("A");
		a.add("B");
		a.add("C");
		a.add("D");
		
		Collection<String> b = new ArrayList<String>(); 
		b.add("A");
		b.add("B");
		b.add("D");
		
		long time= System.nanoTime();
		CollectionUtils.intersect(a, b);
		long del = System.nanoTime() - time;
		
		a = new HashSet<String>(); 
		a.add("A");
		a.add("B");
		a.add("C");
		a.add("D");
		
		time= System.nanoTime();
		CollectionUtils.intersect(a, b);
		long del2 = System.nanoTime() - time;
		
		assertTrue("Faster" , del2<del);
	
	}
	
	@Test
	public void testSpecialIterator(){
		
		List<List<Integer>> lists = new ArrayList<List<Integer>>(); 
		
		List<Integer> a = new ArrayList<Integer>();
		a.add(1);
		a.add(2);
		a.add(3);
		
		List<Integer> b = new ArrayList<Integer>();
		b.add(-1);
		b.add(-2);
		b.add(-3);
		b.add(-4);
		
		lists.add(a);
		lists.add(b);
		
		IteratorsIterator<Integer>  it = new IteratorsIterator<Integer>(lists);
		
		int count =0;
		for (;it.hasNext();){
			it.next();
			count++;
		}
		
		assertEquals(7, count);
	}
}

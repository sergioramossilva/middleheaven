package org.middleheaven.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;
import org.middleheaven.util.CollectionUtils;


public class CollectionUtilsTest {

	@Test
	public void addToArray(){
		String[] sample = {"a","b"};
		
		String[] newsample = CollectionUtils.addToArray(sample, "c");
		
		assertEquals(3,newsample.length);
		assertEquals("c",newsample[2]);
		
		newsample = CollectionUtils.addToArray(sample, "c", "d", "e");
		
		assertEquals(5,newsample.length);
		assertEquals("c",newsample[2]);
		assertEquals("d",newsample[3]);
		assertEquals("e",newsample[4]);
	}
	
	@Test
	public void dinamicArrayCreation(){

		String[] newsample = CollectionUtils.newArray(String.class, 3);
		
		assertEquals(3,newsample.length);

	}
	
	@Test
	public void testIntersect (){
		
		Collection<String> a = new ArrayList<String>(); 
		a.add("A");
		a.add("B");
		a.add("C");
		a.add("D");
		
		Collection<String> b = new ArrayList<String>(); 
		b.add("A");
		b.add("B");
		b.add("D");
		
		assertTrue(CollectionUtils.equalContents(b, CollectionUtils.intersect(a, b)));
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
	
}

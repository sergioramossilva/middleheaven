package org.middleheaven.test.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.middleheaven.util.IteratorsIterator;


public class UtilsTest {

	
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
			Integer i = it.next();
			count++;
		}
		
		assertEquals(7, count);
	}
}

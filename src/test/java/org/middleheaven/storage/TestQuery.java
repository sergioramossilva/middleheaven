package org.middleheaven.storage;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.middleheaven.util.collections.CollectionUtils;

public class TestQuery {

	
	@Test
	public void testQuery(){
		
		Query<String> q = new ListQuery<String>(){

			@Override
			protected List<String> list() {
				return Arrays.asList("A","B","C","D","E");
			}
			
		};
		
		assertFalse(q.isEmpty());
		assertEquals(5L, q.count());
		assertEquals("A", q.first());
	
		Query<String> r = q.setRange(1, 2);
		
		assertFalse(r.isEmpty());
		assertEquals(2L, r.count());
		assertEquals("A", r.first());
		assertTrue(CollectionUtils.equalContents(Arrays.asList("A","B"), r.all()));
		
		r = q.setRange(3, 3);
		
		assertFalse(r.isEmpty());
		assertEquals(3L, r.count());
		assertEquals("C", r.first());
		assertTrue(CollectionUtils.equalContents(Arrays.asList("C","D","E"), r.all()));
		
		r = q.setRange(4, 3);
		
		assertFalse(r.isEmpty());
		assertEquals(2L, r.count());
		assertEquals("D", r.first());
		assertTrue(CollectionUtils.equalContents(Arrays.asList("D","E"), r.all()));
		
		r = q.setRange(6, 3);
		
		assertTrue(r.isEmpty());
		assertEquals(0L, r.count());
		assertEquals(null, r.first());
		assertTrue(r.all().isEmpty());
	}
}

package org.middleheaven.util.classification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;
import org.middleheaven.util.collections.EnhancedList;
import org.middleheaven.util.collections.Walker;

public class ClassificationUtilsTest {

	@Test
	public void testMap(){
		List<Integer> all = Arrays.asList(3,4,1,2);
		
		EnhancedCollection<Integer> c = CollectionUtils.enhance(all);
	}
	
	@Test
	public void testClassificationUtils(){
		List<Integer> all = Arrays.asList(3,4,1,2);
		
		EnhancedCollection<Integer> c = CollectionUtils.enhance(all);
		
		assertEquals(4,c.size());
		
		Integer r = c.find(new Predicate<Integer>(){

			@Override
			public Boolean classify(Integer obj) {
				return obj.intValue() % 2 ==0;
			}
			
		});
		
		assertEquals(Integer.valueOf(4),r);
		
		Collection<Integer> ra = c.findAll(new Predicate<Integer>(){

			@Override
			public Boolean classify(Integer obj) {
				return obj.intValue() % 2 ==0;
			}
			
		});
		
		assertEquals(2,ra.size());
		
		final List<Integer> rest = new ArrayList<Integer>();
		c.forEach(new Walker<Integer>(){
			public void doWith(Integer o){
				rest.add(o);
			}
		});
		
		assertEquals(4,rest.size());
		
		assertTrue(CollectionUtils.equalContents(Arrays.asList(1,2,3,4), c.sort()));
	}
	
	@Test
	public void testWalterFilter(){
		EnhancedList<Integer> all = CollectionUtils.enhance(Arrays.asList(3,4,1,2,-2, 5,6));
		List<Integer> proof = Arrays.asList(16,4,4,36);
		
		
		List<Integer> result = all.filter(new Predicate<Integer>(){

			@Override
			public Boolean classify(Integer i) {
				return i.intValue() % 2 == 0; // only the even ones
			}
			
		}).map(new Classifier<Integer, Integer>(){

			@Override
			public Integer classify(Integer i) {
				return i.intValue() * i.intValue(); // square
			}
			
		}).into(new ArrayList<Integer>());
		
		assertTrue(CollectionUtils.equalContents(proof, result));
		
	}
	
}

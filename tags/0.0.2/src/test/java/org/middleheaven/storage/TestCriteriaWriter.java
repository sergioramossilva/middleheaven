package org.middleheaven.storage;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.storage.testdomain.TestFamillyMember;
import org.middleheaven.storage.testdomain.TestSubject;
import org.middleheaven.util.criteria.Criterion;
import org.middleheaven.util.criteria.FieldCriterion;
import org.middleheaven.util.criteria.LogicCriterion;
import org.middleheaven.util.criteria.entity.EntityCriteria;
import org.middleheaven.util.criteria.entity.EntityCriteriaBuilder;
import org.middleheaven.util.criteria.entity.FieldJuntionCriterion;


public class TestCriteriaWriter {

	
	@Test
	public void testJoin(){
		
		EntityCriteria<TestSubject> criteria = EntityCriteriaBuilder.search(TestSubject.class)
		.and("active").eq(true)
		.and("member").navigateTo(TestFamillyMember.class)
			.and("active").not().isNull()
			.back()
		.all();
		
		LogicCriterion lc = criteria.constraints();
		
		assertEquals(2, lc.criteriaCount());
		
		for (Criterion c : lc){
			if (c instanceof FieldJuntionCriterion){
				FieldJuntionCriterion fj = (FieldJuntionCriterion) c;
				assertTrue("No subquery" , fj.getSubCriteria().constraints().criteriaCount() > 0 );
				
			} else if (c instanceof FieldCriterion) {
				assertTrue(true);
			} else {
				assertTrue("Unkown criterion", false);
			}
		}
	}
}

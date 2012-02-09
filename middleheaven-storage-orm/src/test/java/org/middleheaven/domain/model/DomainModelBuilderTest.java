/**
 * 
 */
package org.middleheaven.domain.model;

import org.junit.Test;
import org.middleheaven.storage.testdomain.TestSubject;

/**
 * 
 */
public class DomainModelBuilderTest {


	@Test
	public void testDomainModelReader (){
		
		ClassSet classes = new ClassSet().add(TestSubject.class.getPackage());
		
		new DomainModelBuilder().build(classes);
	}
}

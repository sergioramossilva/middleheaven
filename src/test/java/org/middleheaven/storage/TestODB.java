package org.middleheaven.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;
import org.middleheaven.domain.DomainModelBuilder;
import org.middleheaven.domain.DomainClasses;
import org.middleheaven.domain.DomainModel;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFiles;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.storage.odb.ObjectDataStorage;
import org.middleheaven.storage.testdomain.TestSubject;


public class TestODB {

	
	@Test
	public void testSimpleAdd(){
		ManagedFile dataFile = ManagedFiles.resolveFile(new File("./neodata.data"));
		ObjectDataStorage keeper = new ObjectDataStorage(dataFile);
		
		final DomainModel model = new DomainModelBuilder().build(
				new DomainClasses().add(TestSubject.class)
		);
		
		EntityStore ds = new DomainStore(keeper , model);

		// read all
		Criteria<TestSubject> c = CriteriaBuilder.search(TestSubject.class).all();
		
		Query<TestSubject> q=  ds.createQuery(c);
		
		long previous = q.count();
		
		TestSubject to = new TestSubject();
		to.setName("Name");
		
		TestSubject to2 = ds.store(to);
		
		assertNotNull(to2.getIdentity());
		assertEquals(previous+1,q.count());
	}
}

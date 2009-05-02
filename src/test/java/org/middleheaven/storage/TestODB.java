package org.middleheaven.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;
import org.middleheaven.domain.DomailModelBuilder;
import org.middleheaven.domain.DomainClasses;
import org.middleheaven.domain.DomainModel;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileRepositories;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.storage.odb.ObjectStoreKeeper;


public class TestODB {

	
	@Test
	public void testSimpleAdd(){
		ManagedFile dataFile = ManagedFileRepositories.resolveFile(new File("./neodata.data"));
		ObjectStoreKeeper keeper = new ObjectStoreKeeper(dataFile);
		
		final DomainModel model = new DomailModelBuilder().build(
				new DomainClasses().add(Subject.class)
		);
		
		DataStorage ds = new DomainDataStorage(keeper , model);

		// read all
		Criteria<Subject> c = CriteriaBuilder.search(Subject.class).all();
		
		Query<Subject> q=  ds.createQuery(c);
		
		long previous = q.count();
		
		Subject to = new Subject();
		to.setName("Name");
		
		Subject to2 = ds.store(to);
		
		assertNotNull(to2.getIdentity());
		assertEquals(previous+1,q.count());
	}
}

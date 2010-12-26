package org.middleheaven.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.middleheaven.model.domain.DomainClasses;
import org.middleheaven.model.domain.DomainModel;
import org.middleheaven.model.domain.DomainModelBuilder;
import org.middleheaven.storage.inmemory.InMemoryStorage;
import org.middleheaven.storage.testdomain.TestSubject;
import org.middleheaven.util.criteria.entity.EntityCriteriaBuilder;


public class StorageManagerTeste {

	EntityStore storage;
	
	InMemoryStorage store = new InMemoryStorage();
	
	@Before
	public void setUp(){
		

		final DomainModel model = new DomainModelBuilder().build(
				new DomainClasses().add(TestSubject.class)
		);

		storage = new SessionAwareEntityStore(new StorableStateManager(store,model));
		
	}
	
	@Test
	public void testInsert(){
		
		Query<TestSubject> q = storage.createQuery(EntityCriteriaBuilder.search(TestSubject.class).all());
		TestSubject subj = new TestSubject();
		subj.setBirthdate(new Date());
		subj.setName("Alberto");
		subj = storage.store(subj);
		// verify correct types
		assertTrue (subj instanceof TestSubject);
		assertTrue (subj instanceof Storable);
		
		// very key is set
		Storable p = (Storable)subj;
		
		assertTrue(p.getIdentity()!=null);
		assertEquals( new Long(0), p.getIdentity());
		assertEquals(new Long(1) , new Long(q.count()));
		
		// verify re-insert does nothing
		subj = storage.store(subj);
		assertEquals( new Long(0), p.getIdentity());

		assertEquals(new Long(1) , new Long(q.count()));
		
	}
	
}

//package org.middleheaven.storage;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//import java.util.Date;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.middleheaven.domain.criteria.EntityCriteriaBuilder;
//import org.middleheaven.domain.model.ClassSet;
//import org.middleheaven.domain.model.DomainModel;
//import org.middleheaven.domain.model.DomainModelBuilder;
//import org.middleheaven.domain.query.Query;
//import org.middleheaven.domain.store.DomainStore;
//import org.middleheaven.domain.store.EntityInstance;
//import org.middleheaven.storage.inmemory.InMemoryStorage;
//import org.middleheaven.storage.testdomain.TestSubject;
//
//
//public class StorageManagerTeste {
//
//	DomainStore storage;
//	
//	InMemoryStorage store = new InMemoryStorage();
//	
//	@Before
//	public void setUp(){
//		
//
//		final DomainModel model = new DomainModelBuilder().build(
//				new ClassSet().add(TestSubject.class)
//		);
//
//		storage = new SessionAwareDomainStore(new EntityInstanceStoreManager(store,model));
//		
//	}
//	
//	@Test
//	public void testInsert(){
//		
//		Query<TestSubject> q = storage.createQuery(EntityCriteriaBuilder.search(TestSubject.class).all());
//		TestSubject subj = new TestSubject();
//		subj.setBirthdate(new Date());
//		subj.setName("Alberto");
//		subj = storage.store(subj);
//		// verify correct types
//		assertTrue (subj instanceof TestSubject);
//		assertTrue (subj instanceof EntityInstance);
//		
//		// very key is set
//		EntityInstance p = (EntityInstance)subj;
//		
//		assertTrue(p.getIdentity()!=null);
//		assertEquals( new Long(0), p.getIdentity());
//		assertEquals(new Long(1) , new Long(q.count()));
//		
//		// verify re-insert does nothing
//		subj = storage.store(subj);
//		assertEquals( new Long(0), p.getIdentity());
//
//		assertEquals(new Long(1) , new Long(q.count()));
//		
//	}
//	
//}

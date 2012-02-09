//package org.middleheaven.storage;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import java.io.File;
//
//import org.junit.Test;
//import org.middleheaven.domain.criteria.EntityCriteria;
//import org.middleheaven.domain.criteria.EntityCriteriaBuilder;
//import org.middleheaven.domain.model.ClassSet;
//import org.middleheaven.domain.model.DomainModel;
//import org.middleheaven.domain.model.DomainModelBuilder;
//import org.middleheaven.domain.query.Query;
//import org.middleheaven.domain.store.DomainStore;
//import org.middleheaven.io.repository.ManagedFile;
//import org.middleheaven.storage.odb.ObjectDataStorage;
//import org.middleheaven.storage.testdomain.TestSubject;
//
//
//public class TestODB {
//
//	
//	@Test
//	public void testSimpleAdd(){
//		ManagedFile dataFile = ManagedFiles.resolveFile(new File("./neodata.data"));
//		ObjectDataStorage keeper = new ObjectDataStorage(dataFile);
//		
//		final DomainModel model = new DomainModelBuilder().build(
//				new ClassSet().add(TestSubject.class)
//		);
//		
//		DomainStore ds = new SessionAwareDomainStore(new EntityInstanceStoreManager(keeper,model));
//		
//		// read all
//		EntityCriteria<TestSubject> c = EntityCriteriaBuilder.search(TestSubject.class).all();
//		
//		Query<TestSubject> q=  ds.createQuery(c);
//		
//		long previous = q.count();
//		
//		TestSubject to = new TestSubject();
//		to.setName("Name");
//		
//		TestSubject to2 = ds.store(to);
//		
//		assertNotNull(to2.getIdentity());
//		assertEquals(previous+1,q.count());
//	}
//}

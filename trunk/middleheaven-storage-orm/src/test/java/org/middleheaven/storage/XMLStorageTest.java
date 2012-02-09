//package org.middleheaven.storage;
//
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
//import org.junit.Test;
//import org.middleheaven.core.wiring.activation.SetActivatorScanner;
//import org.middleheaven.domain.criteria.EntityCriteriaBuilder;
//import org.middleheaven.domain.model.ClassSet;
//import org.middleheaven.domain.model.DomainModel;
//import org.middleheaven.domain.model.DomainModelBuilder;
//import org.middleheaven.domain.query.Query;
//import org.middleheaven.domain.store.DomainStore;
//import org.middleheaven.io.repository.ManagedFile;
//import org.middleheaven.persistance.db.datasource.DataSourceServiceActivator;
//import org.middleheaven.persistance.xml.XMLDataStoreSchema;
//import org.middleheaven.sequence.service.FileSequenceStorageActivator;
//import org.middleheaven.storage.model.DomainWrapPersistableModelReader;
//import org.middleheaven.storage.testdomain.TestSubject;
//import org.middleheaven.tool.test.MiddleHeavenTestCase;
//
//
//public class XMLStorageTest extends MiddleHeavenTestCase{
//
//	static DomainStore ds;
//	
//	protected void configurateTest(SetActivatorScanner scanner) {
//
//		// Activator
//		scanner.addActivator(DataSourceServiceActivator.class);
//		scanner.addActivator(FileSequenceStorageActivator.class);
//		
//		// Configured
//		
//		final DomainModel model = new DomainModelBuilder().build(
//				new ClassSet().add(TestSubject.class)
//		);
//		
//		ManagedFile source = ManagedFiles.resolveFile(this.getClass().getResource("data.xml"));
//		final XMLDataStoreSchema xmlStore = XMLDataStoreSchema.manage(source, new DomainWrapPersistableModelReader(model));
//		ds = new SessionAwareDomainStore(new EntityInstanceStoreManager(xmlStore , model));
//	}
//	
//	@Test 
//	public void test(){
//		Query<TestSubject> q = ds.createQuery(EntityCriteriaBuilder.search(TestSubject.class).all());
//		
//		assertTrue(q.count()>1);
//		
//		TestSubject a = q.fetchFirst();
//		
//		assertTrue(a.getName().equals("Ana"));
//		assertTrue(a.getBirthdate()!=null);
//		
//		q = ds.createQuery(EntityCriteriaBuilder.search(TestSubject.class)
//				.and("name").not().startsWith("João")
//				.and("activo").not().eq(false)
//				.all()
//		);
//		
//		assertTrue(q.count()==1);
//		
//		TestSubject b = q.fetchFirst();
//		
//		assertFalse(a==b);
//		assertTrue(b.getName().equals("Ana"));
//		
//		q = ds.createQuery(EntityCriteriaBuilder.search(TestSubject.class)
//				.and("name").eq("Ana")
//				.and("activo").eq(false)
//				.all()
//		);
//		
//		assertTrue(q.isEmpty());
//	}
//}

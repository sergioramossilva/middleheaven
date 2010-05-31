package org.middleheaven.storage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.core.wiring.activation.SetActivatorScanner;
import org.middleheaven.domain.DomainClasses;
import org.middleheaven.domain.DomainModel;
import org.middleheaven.domain.DomainModelBuilder;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFiles;
import org.middleheaven.sequence.service.FileSequenceStorageActivator;
import org.middleheaven.storage.db.datasource.DataSourceServiceActivator;
import org.middleheaven.storage.testdomain.TestSubject;
import org.middleheaven.storage.xml.XMLStorage;
import org.middleheaven.tool.test.MiddleHeavenTestCase;
import org.middleheaven.util.criteria.entity.EntityCriteriaBuilder;


public class XMLStorageTest extends MiddleHeavenTestCase{

	static EntityStore ds;
	
	protected void configurateTest(SetActivatorScanner scanner) {

		// Activator
		scanner.addActivator(DataSourceServiceActivator.class);
		scanner.addActivator(FileSequenceStorageActivator.class);
		
		// Configured
		
		final DomainModel model = new DomainModelBuilder().build(
				new DomainClasses().add(TestSubject.class)
		);
		
		ManagedFile source = ManagedFiles.resolveFile(this.getClass().getResource("data.xml"));
		final XMLStorage xmlStore = XMLStorage.manage(source, new WrappStorableReader(model));
		ds = new SessionAwareEntityStore(new StorableStateManager(xmlStore , model));
	}
	
	@Test 
	public void test(){
		Query<TestSubject> q = ds.createQuery(EntityCriteriaBuilder.search(TestSubject.class).all());
		
		assertTrue(q.count()>1);
		
		TestSubject a = q.fetchFirst();
		
		assertTrue(a.getName().equals("Ana"));
		assertTrue(a.getBirthdate()!=null);
		
		q = ds.createQuery(EntityCriteriaBuilder.search(TestSubject.class)
				.and("name").not().startsWith("João")
				.and("activo").not().eq(false)
				.all()
		);
		
		assertTrue(q.count()==1);
		
		TestSubject b = q.fetchFirst();
		
		assertFalse(a==b);
		assertTrue(b.getName().equals("Ana"));
		
		q = ds.createQuery(EntityCriteriaBuilder.search(TestSubject.class)
				.and("name").eq("Ana")
				.and("activo").eq(false)
				.all()
		);
		
		assertTrue(q.isEmpty());
	}
}

package org.middleheaven.storage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.core.wiring.activation.SetActivatorScanner;
import org.middleheaven.domain.DomailModelBuilder;
import org.middleheaven.domain.DomainClasses;
import org.middleheaven.domain.DomainModel;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileRepositories;
import org.middleheaven.sequence.service.FileSequenceStorageActivator;
import org.middleheaven.storage.StorageManagerTeste.TestSubject;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.storage.datasource.DataSourceServiceActivator;
import org.middleheaven.storage.xml.XMLStoreKeeper;
import org.middleheaven.tool.test.MiddleHeavenTestCase;


public class XMLStorageTest extends MiddleHeavenTestCase{

	static DataStorage ds;
	
	protected void configurateTest(SetActivatorScanner scanner) {

		// Activator
		scanner.addActivator(DataSourceServiceActivator.class);
		scanner.addActivator(FileSequenceStorageActivator.class);
		
		// Configured
		
		final DomainModel model = new DomailModelBuilder().build(
				new DomainClasses().add(TestSubject.class)
		);
		
		ManagedFile source = ManagedFileRepositories.resolveFile(this.getClass().getResource("data.xml"));
		ds = new DomainDataStorage(XMLStoreKeeper.manage(source, new WrappStorableReader()) , model);
	}
	
	@Test 
	public void test(){
		Query<Subject> q = ds.createQuery(CriteriaBuilder.search(Subject.class).all());
		
		assertTrue(q.count()>1);
		
		Subject a = q.find();
		
		assertTrue(a.getName().equals("Ana"));
		assertTrue(a.getBirthdate()!=null);
		
		q = ds.createQuery(CriteriaBuilder.search(Subject.class)
				.and("name").not().startsWith("João")
				.and("activo").not().eq(false)
				.all()
		);
		
		assertTrue(q.count()==1);
		
		Subject b = q.find();
		
		assertFalse(a==b);
		assertTrue(b.getName().equals("Ana"));
		
		q = ds.createQuery(CriteriaBuilder.search(Subject.class)
				.and("name").eq("Ana")
				.and("activo").eq(false)
				.all()
		);
		
		assertTrue(q.isEmpty());
	}
}

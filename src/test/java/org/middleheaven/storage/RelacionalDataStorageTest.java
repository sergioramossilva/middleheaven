package org.middleheaven.storage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.middleheaven.core.bootstrap.BootstapListener;
import org.middleheaven.core.bootstrap.BootstrapEvent;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.activation.SetActivatorScanner;
import org.middleheaven.domain.DomainClasses;
import org.middleheaven.domain.DomainModel;
import org.middleheaven.domain.DomainModelBuilder;
import org.middleheaven.sequence.service.FileSequenceStorageActivator;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.storage.db.DataBaseStorage;
import org.middleheaven.storage.db.datasource.DataSourceServiceActivator;
import org.middleheaven.storage.db.datasource.EmbeddedDSProvider;
import org.middleheaven.storage.testdomain.TestFamillyMember;
import org.middleheaven.storage.testdomain.TestRelation;
import org.middleheaven.storage.testdomain.TestSubject;
import org.middleheaven.tool.test.MiddleHeavenTestCase;


public class RelacionalDataStorageTest extends MiddleHeavenTestCase {

	static EntityStore ds;
	static Query<TestSubject> query;

	protected void configurateTest(SetActivatorScanner scanner) {


		// Activator
		scanner.addActivator(DataSourceServiceActivator.class);
		scanner.addActivator(FileSequenceStorageActivator.class);

		// Configured

		String catalog = "unit_test_data";

		final EmbeddedDSProvider provider;

		provider = EmbeddedDSProvider.provider(catalog);

		provider.start();

		final DomainModel model = new DomainModelBuilder().build(
				new DomainClasses().add(TestSubject.class.getPackage())
		);

		DataBaseStorage storeManager = new DataBaseStorage(provider, new WrappStorableReader());

		storeManager.updateMetadata(model, catalog);

		ds = new DomainStore(storeManager , model);

		final AtomicInteger counter = new AtomicInteger(9);
		ServiceRegistry.getService(BootstrapService.class).addListener(new BootstapListener(){

			@Override
			public void onBoostapEvent(BootstrapEvent event) {
				if (event.isBootdown()){
					counter.decrementAndGet();
					if( counter.intValue() == 0){
						provider.stop();
					}
				}
			}

		});

		Criteria<TestSubject> all = CriteriaBuilder.search(TestSubject.class).all();
		query = ds.createQuery(all);

	}

	private void assetSubjectStoreIsEmpty(){
		assertTrue("Store is not empty. Remaining " + query.count(), query.count() == 0);
	}

	private void assetSubjectStoreHasElements(int count){
		assertTrue("Store does not have " + count + " elements", query.count() == count);
	}

	@Test
	public void testInsertDelete(){

		TestFamillyMember m = new TestFamillyMember();
		m.setName("Member a");
		TestRelation relation = new TestRelation();
		relation.setName("brother");
		m.setRelation(relation);
		
		TestSubject to = new TestSubject();
		to.setName("Name");
		to.setBirthdate(new Date());
		
		to.addFamillyMember(m);
		
		assetSubjectStoreIsEmpty();

		// store it
		to = ds.store(to);
		TestFamillyMember tm = to.getTestFamillyMembers().iterator().next();
		
		assertNotNull(ds.getIdentityFor(tm));
		
		assetSubjectStoreHasElements(1);

		Query<TestFamillyMember> q1 = ds.createQuery(CriteriaBuilder.search(TestFamillyMember.class).all());
		TestFamillyMember tfm = q1.find();
		
		assertNotNull(tfm.getRelation());
		
		assertNotNull(tfm.getParent());
		
		
		Query<TestSubject> q2 = ds.createQuery(CriteriaBuilder.search(TestSubject.class).all());
		TestSubject tor = q2.find();
		
		assertFalse(tor.getTestFamillyMembers().isEmpty());
		
		tm = tor.getTestFamillyMembers().iterator().next();
		
		assertNotNull(tm);
		
		assertNotNull(ds.getIdentityFor(tm));
		
		// remove it
		ds.remove(to);

		assetSubjectStoreIsEmpty();

	}

	private void assertNotEmpty(TestRelation relation) {
		// TODO implement RelacionalDataStorageTest.assertNotEmpty
		
	}


}

package org.middleheaven.storage;

import static org.junit.Assert.assertEquals;
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
import org.middleheaven.model.domain.DomainClasses;
import org.middleheaven.model.domain.DomainModel;
import org.middleheaven.model.domain.DomainModelBuilder;
import org.middleheaven.sequence.service.FileSequenceStorageActivator;
import org.middleheaven.storage.db.DataBaseStorage;
import org.middleheaven.storage.db.datasource.DataSourceServiceActivator;
import org.middleheaven.storage.db.datasource.EmbeddedDSProvider;
import org.middleheaven.storage.testdomain.TestFamillyMember;
import org.middleheaven.storage.testdomain.TestRelation;
import org.middleheaven.storage.testdomain.TestSubject;
import org.middleheaven.tool.test.MiddleHeavenTestCase;
import org.middleheaven.util.criteria.entity.EntityCriteria;
import org.middleheaven.util.criteria.entity.EntityCriteriaBuilder;


public class RelacionalDataStorageTest extends MiddleHeavenTestCase {

	static EntityStore ds;
	static Query<TestSubject> query;

	protected void configurateTest(SetActivatorScanner scanner) {


		// Activator
		scanner.addActivator(DataSourceServiceActivator.class);
		scanner.addActivator(FileSequenceStorageActivator.class);

		// Configured

		String catalog = "utest_data";

		final EmbeddedDSProvider provider;

		provider = EmbeddedDSProvider.provider(catalog);

		provider.start();

		final DomainModel model = new DomainModelBuilder().build(
				new DomainClasses().add(TestSubject.class.getPackage())
		);

		DataBaseStorage storeManager = new DataBaseStorage(provider.getDataSource(), new WrappStorableReader(model));

		storeManager.updateMetadata(model, catalog);

		ds =  new SessionAwareEntityStore(new StorableStateManager(storeManager,model));
		
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

		EntityCriteria<TestSubject> all = EntityCriteriaBuilder.search(TestSubject.class).all();
		query = ds.createQuery(all);

	}

	private void assetSubjectStoreIsEmpty(){
		assertTrue("Store is not empty. Remaining " + query.count(), query.count() == 0);
	}

	private void assetSubjectStoreHasElements(int count){
		assertTrue("Store does not have " + count + " elements", query.count() == count);
	}

	private void assertIdentical(Object a, Object b){
		assertEquals("Objects are not identical" , ds.getIdentityFor(a),ds.getIdentityFor(b));
	}
	
	@Test
	public void testInsertDelete(){

		TestFamillyMember familly = new TestFamillyMember();
		familly.setName("Member a");
		
		TestRelation relation = new TestRelation();
		relation.setName("brother");
		familly.setRelation(relation);
		
		TestSubject to = new TestSubject();
		to.setName("Name");
		to.setBirthdate(new Date());
		
		to.addFamillyMember(familly);
		
		assetSubjectStoreIsEmpty();

		// store it
		to = ds.store(to);

		// assert was stored
		assetSubjectStoreHasElements(1);

		Query<TestRelation> qr = ds.createQuery(EntityCriteriaBuilder.search(TestRelation.class).all());
		
		// relation is stored by aggregation
		assertTrue(qr.count() == 1 );
		
		// find it
		TestRelation relation2 = qr.fetchFirst();
		
		assertNotNull(relation2);
		
		// search it by identity
		qr = ds.createQuery(EntityCriteriaBuilder.search(TestRelation.class)
				.and("identity").eq(ds.getIdentityFor(relation2))
				.all());
		
		TestRelation relation3 = qr.fetchFirst();
		assertNotNull(relation3);
		
		// is the same as before
		assertIdentical(relation2,relation3);

		// assert the members where merged
		TestFamillyMember tm = to.getTestFamillyMembers().iterator().next();
		
		assertNotNull(ds.getIdentityFor(tm));
		
		// members where stored by aggregation
		Query<TestFamillyMember> q1 = ds.createQuery(EntityCriteriaBuilder.search(TestFamillyMember.class).all());
		
		// find it
		TestFamillyMember tfm = q1.fetchFirst();
		
		assertNotNull(tfm);
		assertNotNull("Relation is null" , tfm.getRelation());
		
		// the relation is the same as before
		assertIdentical(relation2,tfm.getRelation());
		
		// the parent is set
		assertNotNull(tfm.getParent());
		
		// and is the one we stored
		assertIdentical(to,tfm.getParent());
		
		// read the stored object
		Query<TestSubject> q2 = ds.createQuery(EntityCriteriaBuilder.search(TestSubject.class).all());
		TestSubject tor = q2.fetchFirst();
		
		// members are loaded
		assertFalse("Members are empty", tor.getTestFamillyMembers().isEmpty());
		
		tm = tor.getTestFamillyMembers().iterator().next();
		
		assertNotNull(tm);
		
		assertNotNull(ds.getIdentityFor(tm));
		
		// remove it
		ds.remove(to);

		assetSubjectStoreIsEmpty();

	}




}

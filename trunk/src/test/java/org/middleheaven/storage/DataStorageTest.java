package org.middleheaven.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.middleheaven.storage.testdomain.TestSubject;
import org.middleheaven.tool.test.MiddleHeavenTestCase;


public class DataStorageTest extends MiddleHeavenTestCase {

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

		//	DataSource datasource = ServiceRegistry.getService(DataSourceService.class).getDataSource("test");
		DataBaseStorage storeManager = new DataBaseStorage(provider, new WrappStorableReader(model));

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

	private void assetStoreIsEmpty(){
		assertTrue("Store is not empty. Remaining " + query.count(), query.count() == 0);
	}

	private void assertStoreHasElements(int count){
		assertTrue("Store does not have " + count + " elements", query.count() == count);
	}

	@Test
	public void testInsertDelete(){
		Date birthdate = new Date();
		
		TestSubject to = new TestSubject();
		to.setName("Name");
		to.setBirthdate(birthdate);
		to.setNumber(10);


		assetStoreIsEmpty();

		// store it
		ds.store(to);

		to = ds.createQuery(CriteriaBuilder.search(TestSubject.class).all()).find();
		
		assertTrue(to.getNumber() == 10);
		assertTrue(to.getBirthdate().equals(birthdate));
		
		assertStoreHasElements(1);

		// remove it
		ds.remove(to);

		assetStoreIsEmpty();

	}

	@Test
	public void testCriteriaDelete(){

		TestSubject to = new TestSubject();
		to.setName("Name");
		to.setBirthdate(new Date());
		
		assetStoreIsEmpty();

		// store it
		ds.store(to);
		assertStoreHasElements(1);

		Criteria<TestSubject> all = CriteriaBuilder.search(TestSubject.class).all();

		// remove them
		ds.remove(all);

		assetStoreIsEmpty();


	}


	@Test
	public void testInsertUpdate(){

		assetStoreIsEmpty();
		
		TestSubject to = new TestSubject();
		to.setName("Name");
		to = ds.store(to);
		
		assertStoreHasElements(1);

		to.setName("Name 4");
		to = ds.store(to);

		assertStoreHasElements(1);
		
		TestSubject to2 = query.find();
		
		assertEquals("Name 4", to2.getName());
		
		Criteria<TestSubject> all = CriteriaBuilder.search(TestSubject.class).all();

		// remove them
		ds.remove(all);
		
		assetStoreIsEmpty();
	}

	@Test
	public void testOrderby(){

		assetStoreIsEmpty();
		
		TestSubject toA = new TestSubject();
		toA.setName("A");
		toA.setNumber(30);
		
		toA = ds.store(toA);

		TestSubject toB = new TestSubject();
		toB.setName("B");
		toB.setNumber(20);
		toB = ds.store(toB);

		TestSubject toC = new TestSubject();
		toC.setName("C");
		toC.setNumber(10);
		toC = ds.store(toC);
		
		assertStoreHasElements(3);
		
		// order by name
		Criteria<TestSubject> some = CriteriaBuilder.search(TestSubject.class)
		.orderBy("name").asc()
		.all();

		Query<TestSubject> q = ds.createQuery(some);

		String[] names = {"A", "B" , "C"};
		List<TestSubject> objects = new ArrayList<TestSubject>(q.findAll());
		
		assertEquals(names.length, objects.size());
		
		for (int i =0; i < names.length;i++){
			assertEquals(names[i],objects.get(i).getName());
		}
		
		some = CriteriaBuilder.search(TestSubject.class)
		.orderBy("name").desc()
		.all();

		q = ds.createQuery(some);

		names = new String[]{"C", "B" , "A"};
		objects = new ArrayList<TestSubject>(q.findAll());
		
		assertEquals(names.length, objects.size());
		
		for (int i =0; i < names.length;i++){
			assertEquals(names[i],objects.get(i).getName());
		}
		
	}
	
	@Test
	public void testSelectCriteria(){

		assetStoreIsEmpty();
		
		TestSubject toA = new TestSubject();
		toA.setName("Name A");
		toA.setNumber(10);
		
		toA = ds.store(toA);

		TestSubject toB = new TestSubject();
		toB.setName("Name B");
		toA.setNumber(20);
		toB = ds.store(toB);

		TestSubject toC = new TestSubject();
		toC.setName("Name C");
		toC = ds.store(toC);
		
		assertStoreHasElements(3);
		
		// select all not named Name C
		Criteria<TestSubject> some = CriteriaBuilder.search(TestSubject.class)
		.and("name").not().eq("Name A")
		.orderBy("name").asc()
		.all();

		Query<TestSubject> q = ds.createQuery(some);

		assertEquals(2L, q.count());

		some = CriteriaBuilder.search(TestSubject.class)
		.and("name").eq("Name A")
		.orderBy("name").asc()
		.all();

		q = ds.createQuery(some);

		assertEquals(1L, q.count());
		
		TestSubject rtoA = q.find();
		assertTrue("Loaded number is not read", rtoA.getNumber() == 10);

		assertStoreHasElements(3);
		ds.remove(toA);
		assertStoreHasElements(2);
		ds.remove(toB);
		assertStoreHasElements(1);
		
		ds.remove(toC);
		
		assetStoreIsEmpty();
	}

	@Test
	public void testSelectLimit(){

		TestSubject toA = new TestSubject();
		toA.setName("Name A");
		toA = ds.store(toA);

		TestSubject toB = new TestSubject();
		toB.setName("Name B");
		toB = ds.store(toB);

		TestSubject toc = new TestSubject();
		toc.setName("Name C");
		toc = ds.store(toc);


		Criteria<TestSubject> some = CriteriaBuilder.search(TestSubject.class).limit(2).all();

		Query<TestSubject> q = ds.createQuery(some);

		assertEquals(Integer.valueOf(2) , q.findAll().size());


		some = some.duplicate()
		.setRange(2,1);

		q = ds.createQuery(some);

		assertEquals(1 ,  q.findAll().size());

		ds.remove(toA);
		ds.remove(toB);
		ds.remove(toc);
		
		assetStoreIsEmpty();
	}

	@Test
	public void testCriteriaClone(){

		TestSubject toA = new TestSubject();
		toA.setName("Name A");
		toA = ds.store(toA);

		TestSubject toB = new TestSubject();
		toB.setName("Name B");
		toB = ds.store(toB);

		TestSubject toc = new TestSubject();
		toc.setName("Name C");
		toc = ds.store(toc);
		
		Criteria<TestSubject> some = CriteriaBuilder.search(TestSubject.class).limit(3).all();

		some = some.duplicate()
		.setRange(2);

		Query<TestSubject> q = ds.createQuery(some);

		assertEquals(3L , q.count());
		assertEquals(2 , q.findAll().size());
	}

	@Test
	public void testQueryReuse(){

		Criteria<TestSubject> all = CriteriaBuilder.search(TestSubject.class).all();
		Query<TestSubject> q = ds.createQuery(all);

		long count = q.count(); 

		TestSubject to = new TestSubject();
		to.setName("Name");
		to.setBirthdate(new Date());
		to = ds.store(to);

		assertEquals(count+1,q.count());

		count = q.count(); 
		ds.remove(to);

		assertEquals(count-1L, q.count());

	}

}

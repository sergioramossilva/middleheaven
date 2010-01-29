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
import org.middleheaven.core.wiring.annotations.Name;
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
import org.middleheaven.transactions.Transaction;
import org.middleheaven.transactions.TransactionService;


public class DataStorageTest extends MiddleHeavenTestCase {


	static Query<TestSubject> queryAll;
	static TransactionService transactionService;
	static EntityStoreService service;
	
	protected void configurateActivators(SetActivatorScanner scanner) {
		// Activator
		scanner.addActivator(DomainStoreServiceActivator.class);
		scanner.addActivator(DataSourceServiceActivator.class);
		scanner.addActivator(FileSequenceStorageActivator.class);

	}

	protected void configurateTest() {

		// Configured

		String catalog = "unit_test_data";

		final EmbeddedDSProvider provider;

		provider = EmbeddedDSProvider.provider(catalog);

		provider.start();

		final DomainModel model = new DomainModelBuilder().build(
				new DomainClasses().add(TestSubject.class.getPackage())
		);

		//	DataSource datasource = ServiceRegistry.getService(DataSourceService.class).getDataSource("test");
		DataBaseStorage dataStorage = new DataBaseStorage(provider, new WrappStorableReader(model));

		dataStorage.updateMetadata(model, catalog);

		transactionService = ServiceRegistry.getService(TransactionService.class);

		service = ServiceRegistry.getService(EntityStoreService.class);
		service.registerStore("ds", dataStorage, model);

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
		queryAll = service.getStore().createQuery(all);

	}

	private void assetStoreIsEmpty(){
		assertTrue("Store is not empty. Remaining " + queryAll.count(), queryAll.count() == 0);
	}

	private void assertStoreHasElements(int count){
		assertTrue("Store does not have " + count + " elements", queryAll.count() == count);
	}

	
	@Test
	public void testDSInjection(){
		
		DSInjectionTester tester = this.getWiringService().getObjectPool().getInstance(DSInjectionTester.class);
		
		assertTrue(tester.test());
	}
	
	public static class DSInjectionTester {

		private EntityStore store;

		
		public DSInjectionTester (@Name("ds") EntityStore store){
			this.store = store;
		}
		
		public boolean test() {
			return store != null;
		}
		
	}
	
	@Test
	public void testInsertDelete(){
		Date birthdate = new Date();

		TestSubject to = new TestSubject();
		to.setName("Name");
		to.setBirthdate(birthdate);
		to.setNumber(10);


		assetStoreIsEmpty();

		storeIt(to);

		assertStoreHasElements(1);

		to = service.getStore().createQuery(CriteriaBuilder.search(TestSubject.class).all()).first();

		assertTrue(to.getNumber() == 10);
		assertTrue(to.getBirthdate().equals(birthdate));

		removeIt(to);
		
		
		assetStoreIsEmpty();


	}

	@Test
	public void testCriteriaDelete(){

		final TestSubject to = new TestSubject();
		to.setName("Name");
		to.setBirthdate(new Date());

		assetStoreIsEmpty();

		storeIt(to);

		assertStoreHasElements(1);

		clean();

	}

	private void clean(){
		final Criteria<TestSubject> all = CriteriaBuilder.search(TestSubject.class).all();

		Transaction t = transactionService.getTransaction();
		try {
			t.begin();

			service.getStore().remove(all);

			t.commit();


		} catch (RuntimeException e){
			t.roolback();
			throw e;
		}
	
		assetStoreIsEmpty();
	}


	public <T> T storeIt(T to ){
		Transaction t = transactionService.getTransaction();
		try {
			t.begin();

			to =  service.getStore().store(to);

			t.commit();

			return to;
		} catch (RuntimeException e){
			t.roolback();
			throw e;
		}
	}


	public void removeIt(Object to ){
		Transaction t = transactionService.getTransaction();
		try {
			t.begin();

			service.getStore().remove(to);

			t.commit();

		} catch (RuntimeException e){
			t.roolback();
			throw e;
		}
	}


	@Test
	public void testInsertUpdate(){

		assetStoreIsEmpty();

		TestSubject to = new TestSubject();
		to.setName("Name");

		to = storeIt(to);

		assertStoreHasElements(1);

		to.setName("Name 4");

		storeIt(to);

		assertStoreHasElements(1);

		TestSubject to2 = queryAll.first();

		assertEquals("Name 4", to2.getName());

		clean();
	}

	@Test
	public void testOrderby(){

		assetStoreIsEmpty();

		TestSubject toA = new TestSubject();
		toA.setName("A");
		toA.setNumber(30);

		toA = storeIt(toA);
		
		TestSubject toB = new TestSubject();
		toB.setName("B");
		toB.setNumber(20);
	
		toB = storeIt(toB);
		
		TestSubject toC = new TestSubject();
		toC.setName("C");
		toC.setNumber(10);

		toC = storeIt(toC);
		
		assertStoreHasElements(3);

		// order by name
		Criteria<TestSubject> some = CriteriaBuilder.search(TestSubject.class)
		.orderBy("name").asc()
		.all();

		Query<TestSubject> q = service.getStore().createQuery(some);

		String[] names = {"A", "B" , "C"};
		List<TestSubject> objects = new ArrayList<TestSubject>(q.all());

		assertEquals(names.length, objects.size());

		for (int i =0; i < names.length;i++){
			assertEquals(names[i],objects.get(i).getName());
		}

		some = CriteriaBuilder.search(TestSubject.class)
		.orderBy("name").desc()
		.all();

		q = service.getStore().createQuery(some);

		names = new String[]{"C", "B" , "A"};
		objects = new ArrayList<TestSubject>(q.all());

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

		toA = storeIt(toA);

		TestSubject toB = new TestSubject();
		toB.setName("Name B");
		toA.setNumber(20);
		
		toB = storeIt(toB);
		
		TestSubject toC = new TestSubject();
		toC.setName("Name C");

		toC = storeIt(toC);
		
		assertStoreHasElements(3);

		// select all not named Name C
		Criteria<TestSubject> some = CriteriaBuilder.search(TestSubject.class)
		.and("name").not().eq("Name A")
		.orderBy("name").asc()
		.all();

		Query<TestSubject> q = service.getStore().createQuery(some);

		assertEquals(2L, q.count());

		some = CriteriaBuilder.search(TestSubject.class)
		.and("name").eq("Name A")
		.orderBy("name").asc()
		.all();

		q = service.getStore().createQuery(some);

		assertEquals(1L, q.count());

		TestSubject rtoA = q.first();
		assertTrue("Loaded number is not read", rtoA.getNumber() == 10);

		assertStoreHasElements(3);
		removeIt(toA);

		assertStoreHasElements(2);
		removeIt(toB);
		
		assertStoreHasElements(1);
		removeIt(toC);
	
		assetStoreIsEmpty();
	}

	@Test
	public void testSelectLimit(){

		TestSubject toA = new TestSubject();
		toA.setName("Name A");
		toA = storeIt(toA);
		
		TestSubject toB = new TestSubject();
		toB.setName("Name B");
		toB = storeIt(toB);
		
		TestSubject toc = new TestSubject();
		toc.setName("Name C");
		toc = storeIt(toc);

		Criteria<TestSubject> some = CriteriaBuilder.search(TestSubject.class).limit(2).all();

		Query<TestSubject> q = service.getStore().createQuery(some);

		assertEquals(Integer.valueOf(2) , q.all().size());

		some = some.duplicate()
		.setRange(2,1);

		q = service.getStore().createQuery(some);

		assertEquals(1 ,  q.all().size());

		service.getStore().remove(toA);
		service.getStore().remove(toB);
		service.getStore().remove(toc);

		assetStoreIsEmpty();
	}

	@Test
	public void testCriteriaClone(){

		TestSubject toA = new TestSubject();
		toA.setName("Name A");
		toA = storeIt(toA);

		TestSubject toB = new TestSubject();
		toB.setName("Name B");
		toB = storeIt(toB);

		TestSubject toc = new TestSubject();
		toc.setName("Name C");
		toc = storeIt(toc);

		Criteria<TestSubject> some = CriteriaBuilder.search(TestSubject.class).limit(3).all();

		some = some.duplicate()
		.setRange(2);

		Query<TestSubject> q = service.getStore().createQuery(some);

		assertEquals(3L , q.count());
		assertEquals(2 , q.all().size());
	}

	@Test
	public void testQueryReuse(){

		Criteria<TestSubject> all = CriteriaBuilder.search(TestSubject.class).all();
		Query<TestSubject> q = service.getStore().createQuery(all);

		long count = q.count(); 

		TestSubject to = new TestSubject();
		to.setName("Name");
		to.setBirthdate(new Date());
		to = storeIt(to);

		assertEquals(count+1,q.count());

		count = q.count(); 
		removeIt(to);

		assertEquals(count-1L, q.count());

	}

}

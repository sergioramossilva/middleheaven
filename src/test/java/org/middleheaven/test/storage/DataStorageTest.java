package org.middleheaven.test.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.middleheaven.storage.criteria.CriteriaBuilder.search;

import java.io.File;
import java.util.Date;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.StandaloneBootstrap;
import org.middleheaven.core.bootstrap.client.DesktopUIContainer;
import org.middleheaven.core.services.ServiceContextConfigurator;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.services.engine.ActivatorBagServiceDiscoveryEngine;
import org.middleheaven.domain.DomailModelBuilder;
import org.middleheaven.domain.DomainClasses;
import org.middleheaven.domain.DomainModel;
import org.middleheaven.io.repository.ManagedFileRepositories;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.sequence.service.FileSequenceStorageActivator;
import org.middleheaven.storage.DataStorage;
import org.middleheaven.storage.DomainDataStorage;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.WrappStorableReader;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.datasource.DataSourceService;
import org.middleheaven.storage.datasource.DataSourceServiceActivator;
import org.middleheaven.storage.datasource.DriverManagerDSProvider;
import org.middleheaven.storage.db.DataBaseStoreKeeper;
import org.middleheaven.test.storage.StorageManagerTeste.TestSubject;
import org.middleheaven.tool.test.MiddleHeavenTestCase;


public class DataStorageTest extends MiddleHeavenTestCase {

	static DataStorage ds;
	static boolean runTest=true;

	@Before
	public void setUp(){
		Container container = new DesktopUIContainer(ManagedFileRepositories.resolveFile(new File(".")));
		StandaloneBootstrap bootstrap = new StandaloneBootstrap(this,container);
		bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));

		// Activator
		ActivatorBagServiceDiscoveryEngine engine = new ActivatorBagServiceDiscoveryEngine();
		engine.addActivator(DataSourceServiceActivator.class);
		engine.addActivator(FileSequenceStorageActivator.class);
		new ServiceContextConfigurator().addEngine(engine);

		// Configured

		DataSourceService srv = ServiceRegistry.getService(DataSourceService.class);
		//		srv.addDataSource("test", new DriverDataSource(
		//				"org.postgresql.Driver" ,
		//				"jdbc:postgresql:test" ,
		//				"pguser",
		//				"pguser"
		//		));
		srv.addDataSourceProvider("test", DriverManagerDSProvider.provider(
				"org.hsqldb.jdbcDriver" ,
				"jdbc:hsqldb:F:\\Workspace\\MiddleHeaven_Google\\XTest;shutdown=true" ,
				"sa",
				""
		));


		final DomainModel model = new DomailModelBuilder().build(
				new DomainClasses().add(TestSubject.class)
		);

		DataSource datasource = ServiceRegistry.getService(DataSourceService.class).getDataSource("test");
		ds = new DomainDataStorage(new DataBaseStoreKeeper(datasource, new WrappStorableReader()) , model);

		try{
			datasource.getConnection();
		}catch (Exception e){
			e.printStackTrace();
			runTest = false;	
		}
	}

	@Test
	public void testDelete(){
		if(runTest){
			Subject to = new Subject();
			to.setName("Name");
			to.setBirthdate(new Date());
			// store it
			to = ds.store(to);

			// remove it
			ds.remove(to);
		}


	}

	@Test
	public void testCriteriaDelete(){
		if(runTest){
			Criteria<Subject> all = search(Subject.class).all();

			// remove them
			ds.remove(all);

			Query<Subject> q = ds.createQuery(all);

			assertEquals(0L,q.count());
		}
	}

	@Test
	public void testInsert(){

		Subject to = new Subject();
		to.setName("Name");
		to.setBirthdate(new Date());
		ds.store(to);
	}

	@Test
	public void testUpdate(){
		if(runTest){
			Subject to = new Subject();
			to.setName("Name");
			to = ds.store(to);

			to.setName("Name 4");
			to = ds.store(to);
		}
	}

	@Test
	public void testSelectAll(){

		Criteria<Subject> all = search(Subject.class).all();
		Query<Subject> q = ds.createQuery(all);

		assertTrue(q.count()>0);

		assertNotNull(q.find());

		int count=0;
		for (Subject t : q.list()){
			count++;
			assertTrue(t instanceof Subject);
			assertNotNull(t.getName());
		}
		assertTrue(count == q.count());
	}

	@Test
	public void testSelectCriteria(){

		Subject toA = new Subject();
		toA.setName("Name A");
		toA = ds.store(toA);

		Subject toB = new Subject();
		toB.setName("Name B");
		toB = ds.store(toB);

		Criteria<Subject> some = search(Subject.class)
		.and("name").not().eq("Name A")
		.orderBy("name").asc()
		.all();

		Query<Subject> q = ds.createQuery(some);

		assertEquals(3L, q.count());

		some = search(Subject.class)
		.and("name").eq("Name A")
		.orderBy("name").asc()
		.all();

		q = ds.createQuery(some);

		assertEquals(1L, q.count());

		ds.remove(toA);
		ds.remove(toB);
	}

	@Test
	public void testSelectLimit(){

		Subject toA = new Subject();
		toA.setName("Name A");
		toA = ds.store(toA);

		Subject toB = new Subject();
		toB.setName("Name B");
		toB = ds.store(toB);

		Subject toc = new Subject();
		toc.setName("Name C");
		toc = ds.store(toc);


		Criteria<Subject> some = search(Subject.class).limit(3).all();

		Query<Subject> q = ds.createQuery(some);

		assertEquals(3L , q.count());


		some = some.clone()
		.setRange(2,1);

		q = ds.createQuery(some);

		assertEquals(1L ,  q.count());

		ds.remove(toA);
		ds.remove(toB);
		ds.remove(toc);
	}

	@Test
	public void testCriteriaClone(){

		Criteria<Subject> some = search(Subject.class).limit(3).all();

		some = some.clone()
		.setRange(2);

		Query<Subject> q = ds.createQuery(some);

		assertEquals(2L , q.count());
	}

	@Test
	public void testQueryReuse(){

		Criteria<Subject> all = search(Subject.class).all();
		Query<Subject> q = ds.createQuery(all);

		long count = q.count(); 

		Subject to = new Subject();
		to.setName("Name");
		to.setBirthdate(new Date());
		to = ds.store(to);

		assertEquals(count+1,q.count());

		count = q.count(); 
		ds.remove(to);

		assertEquals(count-1L, q.count());

	}

}

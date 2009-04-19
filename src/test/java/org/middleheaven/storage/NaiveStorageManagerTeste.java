package org.middleheaven.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.core.bootstrap.StandaloneBootstrap;
import org.middleheaven.core.bootstrap.client.StandaloneContainer;
import org.middleheaven.domain.DomailModelBuilder;
import org.middleheaven.domain.DomainClasses;
import org.middleheaven.domain.DomainModel;
import org.middleheaven.io.repository.ManagedFileRepositories;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.storage.DomainDataStorage;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.storage.inmemory.InMemoryStoreKeeper;
import org.middleheaven.util.identity.Identity;


public class NaiveStorageManagerTeste {

	DomainDataStorage manager;
	TestSubject subj = new TestSubject();
	InMemoryStoreKeeper store = new InMemoryStoreKeeper();
	
	@Before
	public void setUp(){
		// bootstrap
		Container container = new StandaloneContainer(ManagedFileRepositories.resolveFile(new File("."))){

			@Override
			public void start(ExecutionEnvironmentBootstrap bootstrap) {
				// TODO implement Container.start
				
			}

			@Override
			public void stop(ExecutionEnvironmentBootstrap bootstrap) {
				// TODO implement Container.stop
				
			}};
			
		StandaloneBootstrap bootstrap = new StandaloneBootstrap(this,container);
		bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));
		
		// create model
		final DomainModel model = new DomailModelBuilder().build(
				new DomainClasses().add(TestSubject.class)
		);
		
		manager = new DomainDataStorage(store , model);
		
		subj.setNascimento(CalendarDate.today());
		subj.setName("Alberto");
	}
	
	@Test
	public void testInsert(){
		
		Query<TestSubject> q = manager.createQuery(CriteriaBuilder.search(TestSubject.class).all());
		
		subj = manager.store(subj);
		// verify correct types
		assertTrue (subj instanceof TestSubject);
		assertTrue (subj instanceof Storable);
		
		// very key is set
		Storable p = (Storable)subj;
		
		assertTrue(p.getIdentity()!=null);

		assertEquals(new Long(1) , new Long(q.count()));
		
		// verify re-insert does nothing
		subj = manager.store(subj);

		assertEquals(new Long(1) , new Long(q.count()));
		
	}
	

	public static class TestSubject {

		private Identity identity;
		private String name;
		private CalendarDate nacimento;
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public CalendarDate getNascimento() {
			return nacimento;
		}
		
		public void setNascimento(CalendarDate nacimento) {
			this.nacimento = nacimento;
		}

		public void setIdentity(Identity identity) {
			this.identity = identity;
		}

		public Identity getIdentity() {
			return identity;
		}
		
	}

}

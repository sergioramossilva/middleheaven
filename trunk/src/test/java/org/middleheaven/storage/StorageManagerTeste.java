package org.middleheaven.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.middleheaven.domain.DomailModelBuilder;
import org.middleheaven.domain.DomainClasses;
import org.middleheaven.domain.DomainModel;
import org.middleheaven.domain.annotations.Key;
import org.middleheaven.storage.DataStorage;
import org.middleheaven.storage.DomainDataStorage;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.storage.inmemory.InMemoryStoreKeeper;
import org.middleheaven.util.identity.Identity;


public class StorageManagerTeste {

	DataStorage storage;
	
	InMemoryStoreKeeper store = new InMemoryStoreKeeper();
	
	@Before
	public void setUp(){
		

		final DomainModel model = new DomailModelBuilder().build(
				new DomainClasses().add(TestSubject.class)
		);
		
		storage = new DomainDataStorage(store , model);
		
	}
	
	@Test
	public void testInsert(){
		
		Query<TestSubject> q = storage.createQuery(CriteriaBuilder.search(TestSubject.class).all());
		TestSubject subj = new TestSubject();
		subj.setNacimento(new Date());
		subj.setName("Alberto");
		subj = storage.store(subj);
		// verify correct types
		assertTrue (subj instanceof TestSubject);
		assertTrue (subj instanceof Storable);
		
		// very key is set
		Storable p = (Storable)subj;
		
		assertTrue(p.getIdentity()!=null);
		assertEquals( new Long(0), p.getIdentity());
		assertEquals(new Long(1) , new Long(q.count()));
		
		// verify re-insert does nothing
		subj = storage.store(subj);
		assertEquals( new Long(0), p.getIdentity());

		assertEquals(new Long(1) , new Long(q.count()));
		
	}
	

	public class TestSubject {

		private Identity identity;
		
		@Key
		public Identity getIdentity() {
			return identity;
		}
		public void setIdentity(Identity identity) {
			this.identity = identity;
		}
		private String name;
		private Date nacimento;
		
		protected String getName() {
			return name;
		}
		protected void setName(String name) {
			this.name = name;
		}
		protected Date getNacimento() {
			return nacimento;
		}
		protected void setNacimento(Date nacimento) {
			this.nacimento = nacimento;
		}
		
	}

}

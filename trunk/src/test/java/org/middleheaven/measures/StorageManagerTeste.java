package org.middleheaven.measures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.middleheaven.storage.DataStorage;
import org.middleheaven.storage.DomainDataStorage;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StoreMetadataManager;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.storage.db.DataBaseStoreKeeper;
import org.middleheaven.storage.inmemory.NaiveStoreKeeper;
import org.middleheaven.storage.model.AnnotationsStorableEntityModel;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IntegerIdentity;


public class StorageManagerTeste {

	DataStorage storage;
	
	NaiveStoreKeeper store = new NaiveStoreKeeper();
	
	@Before
	public void setUp(){
		storage = new DomainDataStorage(store , new StoreMetadataManager(){

			@Override
			public StorableEntityModel getStorageModel(Class<?> type) {
				return new AnnotationsStorableEntityModel(type);
			}

			@Override
			public Class<? extends Identity> indentityTypeFor(
					Class<?> entityType) {
				return IntegerIdentity.class;
			}

		});
		
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

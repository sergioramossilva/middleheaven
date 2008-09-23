package org.middleheaven.test.storage;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.middleheaven.storage.DataStorage;
import org.middleheaven.storage.DomainDataStorage;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.storage.StoreMetadataManager;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.storage.inmemory.SpaceStoreManager;
import org.middleheaven.storage.model.AnnotationsStorableEntityModel;
import org.space4j.Space4J;
import org.space4j.implementation.SimpleSpace4J;


public class SpaceStorageTest {

	static Space4J space4j; 

	@BeforeClass
	public static void setUp(){
		try {
			space4j = new SimpleSpace4J("./test");
			space4j.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}


	@Test
	public void test(){


		DataStorage ds = new DomainDataStorage(new SpaceStoreManager(space4j) , new StoreMetadataManager(){

			@Override
			public StorableEntityModel getStorageModel(Class<?> type) {
				return new AnnotationsStorableEntityModel(type);
			}

		});

		// read all
		Criteria<Subject> c = CriteriaBuilder.search(Subject.class).all();
		
		Query<Subject> q=  ds.createQuery(c);
		
		long previous = q.count();
		
		Subject to = new Subject();
		to.setName("Name");
		ds.store(to);
		
		assertEquals(previous+1,q.count());
		
		
	}
}

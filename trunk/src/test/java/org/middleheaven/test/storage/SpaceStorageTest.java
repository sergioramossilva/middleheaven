package org.middleheaven.test.storage;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.middleheaven.domain.DomailModelBuilder;
import org.middleheaven.domain.DomainClasses;
import org.middleheaven.domain.DomainModel;
import org.middleheaven.storage.DataStorage;
import org.middleheaven.storage.DomainDataStorage;
import org.middleheaven.storage.Query;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.storage.criteria.CriteriaBuilder;
import org.middleheaven.storage.inmemory.SpaceStoreKeeper;
import org.space4j.Space4J;
import org.space4j.implementation.SimpleSpace4J;


public class SpaceStorageTest {

	static Space4J space4j; 
    static File folder;
    
	@BeforeClass
	public static void setUp(){
		try {
			folder = new File("./space4j_db/tests4j");
			space4j = new SimpleSpace4J("./tests4j");
			space4j.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	@AfterClass
	public static void tearDown(){
		try {
			if (folder.exists()){
				folder.delete();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@Test
	public void test(){

		final DomainModel model = new DomailModelBuilder().build(
				new DomainClasses().add(Subject.class)
		);
		
		DataStorage ds = new DomainDataStorage(new SpaceStoreKeeper(space4j) , model);

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

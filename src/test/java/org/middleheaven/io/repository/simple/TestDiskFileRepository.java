package org.middleheaven.io.repository.simple;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;

public class TestDiskFileRepository {

	
	@Test
	public void testIsDrive(){
		
		DiskFileManagedRepository repo = (DiskFileManagedRepository) DiskFileManagedRepository.repository();
		
		assertTrue(repo.isDriveRoot(new File("c:/")));
		assertFalse(repo.isDriveRoot(new File("c:/someFolder")));
	}
	
	@Test(expected = ManagedIOException.class)
	public void testRetrive(){
		
		DiskFileManagedRepository repo = (DiskFileManagedRepository) DiskFileManagedRepository.repository();
		
		repo.retrive("xs:/");
		
	}
	
	@Test
	public void testReadURL() throws MalformedURLException, URISyntaxException{
		DiskFileManagedRepository repo = (DiskFileManagedRepository) DiskFileManagedRepository.repository();
		
		ManagedFile file = repo.retrive("c:/");
		
		assertEquals (new File("C:/").toURI() , file.getURL().toURI());
	}
	
}

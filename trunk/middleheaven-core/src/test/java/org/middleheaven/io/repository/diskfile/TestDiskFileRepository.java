package org.middleheaven.io.repository.diskfile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.io.repository.FileRepositoryService;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.tool.test.MiddleHeavenTestCase;

public class TestDiskFileRepository extends MiddleHeavenTestCase {

	
	private FileRepositoryService service;

	@Wire
	public void setFileRepositoryService (FileRepositoryService service){
		this.service = service;
	}

	@Test
	public void testReadURL() throws MalformedURLException, URISyntaxException{
		ManagedFileRepository repo = service.newRepository(URI.create("file:/c:/"));
		
		ManagedFile file = repo.retrive(repo.getPath("c:"));
		
		assertEquals (new File("c:/").toURI() , file.getURI());
	}
	
}

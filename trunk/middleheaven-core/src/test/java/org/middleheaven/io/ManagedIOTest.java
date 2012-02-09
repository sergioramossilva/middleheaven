package org.middleheaven.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.io.repository.FileRepositoryService;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.machine.MachineFileSystemRepositoryProvider;
import org.middleheaven.io.repository.machine.MachineFiles;
import org.middleheaven.tool.test.MiddleHeavenTestCase;
import org.middleheaven.util.collections.Walker;


public class ManagedIOTest extends MiddleHeavenTestCase {


	private FileRepositoryService fileService;

	@Wire
	public void setFileRepositoryService (FileRepositoryService fileService){
		this.fileService = fileService;
	}
	
	@Test
	public void textLocalRepository() throws ManagedIOException, IOException{
		// select a folder. whatever it is doesn't mater

		
		ManagedFile folder = MachineFiles.getDefaultFolder();
		
		ManagedFile testJar = folder.retrive("test.jar");
		
		if (testJar.exists()){
			testJar.delete();
		}
		
		testJar.createFile();
		
		assertTrue(testJar.exists());

		// this folder must exist as it is used by maven
		
		ManagedFile home = MachineFiles.getUserWorkspaceFolder();
		
		ManagedFile file = home.retrive("/.m2/repository/junit/junit/4.1/junit-4.1.jar");
		    
		ManagedFileRepository jar = fileService.newRepository(URI.create("jar:"+ file.getURI().toString()));
	    
		ManagedFile junitJar = jar.retrive(jar.getPath("/"));
	    
	    junitJar.copyTo(testJar);
	    
	    assertTrue(testJar.getContent().getSize() >0);
	   
	    testJar.delete();
	    
	    assertFalse(testJar.exists());
	    
	    final AtomicInteger count = new  AtomicInteger();
	    
	    junitJar.each(new Walker<ManagedFile>(){

			@Override
			public void doWith(ManagedFile object) {
				count.incrementAndGet();
			}
	    	
	    });
	    
	    assertFalse(count.get() == 0);
	}
	

	@Test
	public void textZipRepository() throws ManagedIOException, IOException{
	
		  ManagedFileRepository junitJar = fileService.newRepository(URI.create("jar:" + new File(System.getProperty("user.home") + "/.m2/repository/junit/junit/4.1/junit-4.1.jar").toURI().toString()));
	    

		   assertTrue(junitJar.isReadable());
		   assertTrue(junitJar.isWriteable());

		   // Extract
		   ManagedFile manifest = junitJar.retrive(junitJar.getPath("META-INF/MANIFEST.MF"));
		   
		   ManagedFile frep = MachineFiles.getDefaultFolder();
			
		   assertTrue(frep.exists());
		   
		   manifest.copyTo(frep);
		   
		   ManagedFile manifestCopy = frep.retrive("MANIFEST.MF");
		   
		   assertTrue(manifestCopy.exists());
		   
		   manifestCopy.delete();
	}
	
	@Test
	public void textHttpRepositoryRead() throws ManagedIOException, IOException{
	
		URI uri = URI.create("http://middleheaven.sourceforge.net/");
		

		ManagedFile localRep = MachineFiles.getDefaultFolder();
		
		ManagedFileRepository rep = fileService.newRepository(uri);
		
		assertTrue(rep.isReadable());
		assertTrue(rep.isWriteable());
		
		ManagedFile faq = rep.retrive(rep.getPath("faq.html"));
		ManagedFile xxx = rep.retrive(rep.getPath("xxx.html"));
		
		assertTrue(faq.exists());
		assertFalse(xxx.exists());
	
		
		ManagedFile localFaq = localRep.retrive("faq.html");
		localFaq.createFile();
		
		assertTrue(localFaq.exists());
		
		faq.copyTo(localFaq);
		
		assertTrue(localFaq.getContent().getSize() >0);
		
		localFaq.delete();

	}
	
//	@Test
//	public void textHttpRepositoryList() throws ManagedIOException, IOException{
//	
//		URL url = new URL("http://middleheaven.sourceforge.net/");
//		
//		ManagedFile http = ManagedFiles.resolveFile(url);
//		
//		// cannot be listed
//		assertFalse(!http.children().isEmpty());
//
//
//	}
	
	
	
}

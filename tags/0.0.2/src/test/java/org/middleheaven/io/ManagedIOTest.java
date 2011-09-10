package org.middleheaven.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFiles;


public class ManagedIOTest {


	@Test
	public void textLocalRepository() throws ManagedIOException, IOException{
		// select a folder. whatever it is doesn't mater

		ManagedFile folder = ManagedFiles.resolveFile(new File("."));

		assertTrue(folder.exists());
		
		ManagedFile testJar = folder.retrive("test.jar");
		
		if (testJar.exists()){
			testJar.delete();
		}
		
		testJar.createFile();
		
		assertTrue(testJar.exists());
		
		// this folder must exist as it is used by maven
	    ManagedFile rep = ManagedFiles.resolveFile(new File(System.getProperty("user.home") + "/.m2/repository/junit/junit/4.1"));
	    
	    
	    ManagedFile junitJar = rep.retrive("junit-4.1.jar");
	    
	    junitJar.copyTo(testJar);
	    
	    assertTrue(testJar.getContent().getSize() >0);
	    
	    testJar.delete();
	    
	    assertFalse(testJar.exists());
	    
	    assertFalse(junitJar.children().isEmpty());
	}
	

	@Test
	public void textZipRepository() throws ManagedIOException, IOException{
	
		   ManagedFile rep = ManagedFiles.resolveFile(new File(System.getProperty("user.home") + "/.m2/repository/junit/junit/4.1"));
		    
		    ManagedFile junitJar = rep.retrive("junit-4.1.jar");

		    assertTrue(junitJar.getType().hasContent());
		    assertTrue(junitJar.getType().hasChildren());
		    assertTrue(junitJar.isReadable());
		    assertTrue(junitJar.isWriteable());
		    
		   assertFalse(junitJar.children().isEmpty());
		   
		   // Extract
		   ManagedFile manifest = junitJar.retrive("META-INF/MANIFEST.MF");
		   
		   ManagedFile frep = ManagedFiles.resolveFile(new File("."));
		   
		   manifest.copyTo(frep);
		   
		   ManagedFile manifestCopy = frep.retrive("MANIFEST.MF");
		   
		   assertTrue(manifestCopy.exists());
		   
		   manifestCopy.delete();
	}
	
	@Test
	public void textHttpRepositoryRead() throws ManagedIOException, IOException{
	
		URL url = new URL("http://middleheaven.sourceforge.net/");
		
		ManagedFile localRep = ManagedFiles.resolveFile(new File("."));
		ManagedFile rep = ManagedFiles.resolveFile(url);
		
		assertTrue(rep.isReadable());
		assertTrue(rep.isWriteable());
		
		ManagedFile faq = rep.retrive("faq.html");
		ManagedFile xxx = rep.retrive("xxx.html");
		
		assertTrue(faq.exists());
		assertFalse(xxx.exists());
	
		
		ManagedFile localFaq = localRep.retrive("faq.html");
		localFaq.createFile();
		
		assertTrue(localFaq.exists());
		
		faq.copyTo(localFaq);
		
		assertTrue(localFaq.getContent().getSize() >0);
		
		localFaq.delete();

	}
	
	@Test
	public void textHttpRepositoryList() throws ManagedIOException, IOException{
	
		URL url = new URL("http://middleheaven.sourceforge.net/");
		
		ManagedFile http = ManagedFiles.resolveFile(url);
		
		// cannot be listed
		assertFalse(!http.children().isEmpty());


	}
	
	
	
}

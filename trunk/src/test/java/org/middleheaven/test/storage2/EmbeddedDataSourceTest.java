package org.middleheaven.test.storage2;


import java.io.File;

import org.junit.Test;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileRepositories;
import org.middleheaven.storage.datasource.EmbeddedDSProvider;


public class EmbeddedDataSourceTest {

	
	@Test
	public void test(){
		
		ManagedFile vfs =  ManagedFileRepositories.resolveFile(new File("."));
		ManagedFile dataFolder = vfs.resolveFile("data/db");
		if (!dataFolder.exists()){
			dataFolder.createFolder();
		}
		
		EmbeddedDSProvider eds = EmbeddedDSProvider.provider( dataFolder.getURL(), "testdb", "sa", "");
		
		eds.start();

	}
}

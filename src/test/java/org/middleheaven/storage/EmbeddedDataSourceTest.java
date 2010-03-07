package org.middleheaven.storage;


import java.io.File;

import org.junit.Test;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFiles;
import org.middleheaven.storage.db.datasource.EmbeddedDSProvider;


public class EmbeddedDataSourceTest {

	
	@Test
	public void test(){
		
		ManagedFile vfs =  ManagedFiles.resolveFile(new File("."));
		ManagedFile dataFolder = vfs.resolveFile("data/db");
		if (!dataFolder.exists()){
			dataFolder.createFolder();
		}
		
		EmbeddedDSProvider eds = EmbeddedDSProvider.provider( "testdb", dataFolder.getURL(), "sa", "");
		
		eds.start();

	}
}

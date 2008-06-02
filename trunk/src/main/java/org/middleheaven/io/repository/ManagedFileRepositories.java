package org.middleheaven.io.repository;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.middleheaven.io.repository.vfs.CommonsVSFRepositoryEngine;

public class ManagedFileRepositories {

	
	private ManagedFileRepositories (){
		
	}
	
	static RepositoryEngine engine = new CommonsVSFRepositoryEngine();
	
	public static void setDefaultEngine(RepositoryEngine defaultEngine){
		engine = defaultEngine;
	}
	
	public static ManagedFile resolveFile(URL url){
		try {
			return engine.getManagedFileResolver().resolveFile(url.toURI().toString());
		} catch (URISyntaxException e) {
			throw new RepositoryCreationException(e);
		}
	}
	
	public static ManagedFile resolveFile(File file){
		return engine.getManagedFileResolver().resolveFile(file.toURI().toString());
	}

}

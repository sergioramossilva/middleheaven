package org.middleheaven.io.repository;

import org.middleheaven.core.reflection.ClassIntrospector;
import org.middleheaven.io.repository.simple.SystemDrivesRepositoryEngine;
import org.middleheaven.io.repository.vfs.CommonsVSFRepositoryEngine;

public class ClasspathRepositoryEngine implements RepositoryEngine {

	ManagedFileResolver resolver;
	
	@Override
	public ManagedFileResolver getManagedFileResolver() throws RepositoryCreationException {
		
		if (resolver == null){
			if (ClassIntrospector.isInClasspath("org.apache.commons.vfs.FileObject")){
				// ok, load 
				resolver = new CommonsVSFRepositoryEngine().getManagedFileResolver();
			} else {
				resolver = new SystemDrivesRepositoryEngine().getManagedFileResolver();
			}
		}
		
		return resolver;
		
		
	}

}

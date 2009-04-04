package org.middleheaven.io.repository;

import org.middleheaven.core.reflection.NoSuchClassReflectionException;
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.io.repository.simple.SystemDrivesRepositoryEngine;
import org.middleheaven.io.repository.vfs.CommonsVSFRepositoryEngine;

public class ClasspathRepositoryEngine implements RepositoryEngine {

	ManagedFileResolver resolver;
	
	@Override
	public ManagedFileResolver getManagedFileResolver() throws RepositoryCreationException {
		
		if (resolver == null){
			try{
				ReflectionUtils.loadClass("org.apache.commons.vfs.FileObject");
				// ok, load 
				resolver = new CommonsVSFRepositoryEngine().getManagedFileResolver();
			} catch (NoSuchClassReflectionException e){
				resolver = new SystemDrivesRepositoryEngine().getManagedFileResolver();
			}
		}
		
		return resolver;
		
		
	}

}

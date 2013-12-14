package org.middleheaven.io.repository;

import java.net.URI;
import java.util.Map;

import org.middleheaven.io.repository.machine.MachineFileSystemRepositoryProvider;
import org.middleheaven.reflection.inspection.ClassIntrospector;

/**
 * 
 */
public class AutoLoadRepositoryProvider implements ManagedFileRepositoryProvider {

	ManagedFileRepositoryProvider provider;
	
	
	public AutoLoadRepositoryProvider (){
		if (ClassIntrospector.isInClasspath("org.apache.commons.vfs.FileObject") && 
				ClassIntrospector.isInClasspath("org.middleheaven.io.repository.vfs.CommonsVSFRepositoryEngine")){
			// ok, load 
			provider =  (ManagedFileRepositoryProvider) ClassIntrospector.loadFrom("org.middleheaven.io.repository.vfs.CommonsVSFRepositoryEngine").newInstance();
		} else {
			provider = MachineFileSystemRepositoryProvider.getProvider();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepository newRepository(URI uri, Map<String, Object> params) throws RepositoryCreationException {
		return provider.newRepository(uri, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getScheme() {
		return "file";
	}

}

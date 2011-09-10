package org.middleheaven.io.repository;

import java.net.URI;
import java.util.Map;

import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.io.repository.engines.FileRepositoryProvider;
import org.middleheaven.io.repository.watch.WatchService;

/**
 * 
 */
public class AutoLoadRepositoryProvider implements FileRepositoryProvider {

	FileRepositoryProvider provider;
	
	
	public AutoLoadRepositoryProvider (){
		if (ClassIntrospector.isInClasspath("org.apache.commons.vfs.FileObject") && 
				ClassIntrospector.isInClasspath("org.middleheaven.io.repository.vfs.CommonsVSFRepositoryEngine")){
			// ok, load 
			provider =  (FileRepositoryProvider) ClassIntrospector.loadFrom("org.middleheaven.io.repository.vfs.CommonsVSFRepositoryEngine").newInstance();
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

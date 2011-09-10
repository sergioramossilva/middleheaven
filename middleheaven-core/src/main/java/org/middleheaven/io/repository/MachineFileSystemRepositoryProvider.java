package org.middleheaven.io.repository;


import java.net.URI;
import java.util.Map;

import org.middleheaven.io.repository.engines.FileRepositoryProvider;

/**
 * {@link FileRepositoryProvider} based upon the machines file system.
 */
public class MachineFileSystemRepositoryProvider implements FileRepositoryProvider {


	private static final MachineFileSystemRepositoryProvider provider = new MachineFileSystemRepositoryProvider();
	
	static MachineFileSystemRepositoryProvider getProvider(){
		return provider;
	}
	
	
	final MachineFileSystemAcessStrategy systemFileStrategy;
	
	/**
	 * 
	 * Constructor.
	 */
	private MachineFileSystemRepositoryProvider(){
		// TODO decide using java version
		// using java.io.File
		this.systemFileStrategy = new FileIOStrategy();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepository newRepository(URI uri, Map<String, Object> params) throws RepositoryCreationException {
		if (uri.getScheme().equals("file")){
			return this.systemFileStrategy.openFileRepository(uri);
		} else {
			throw new UnsupportedSchemeException(uri.getScheme() + " is not supported by " + MachineFileSystemRepositoryProvider.class.getName());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getScheme() {
		return "file";
	}


}

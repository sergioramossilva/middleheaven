package org.middleheaven.io.repository.engines;

import java.net.URI;
import java.util.Map;

import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.RepositoryCreationException;

public interface ManagedFileRepositoryProvider  {


	/**
	 * Open a file repository on the given URI.
	 * 
	 * @param uri
	 * @return
	 * @throws RepositoryCreationException if it was not possible to open the repository. 
	 */
	public ManagedFileRepository newRepository(URI uri, Map<String, Object> params) throws RepositoryCreationException;
	
	/**
	 * The URI scheme this provider can handle.
	 * @return
	 */
	public String getScheme();
}

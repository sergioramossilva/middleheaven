/**
 * 
 */
package org.middleheaven.io.repository;

import java.net.URI;
import java.util.Map;

import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.io.repository.engines.ManagedFileRepositoryProvider;

/**
 * 
 */
@Service
public interface FileRepositoryService {

	
	public void registerProvider(ManagedFileRepositoryProvider provider);
	
	public void unRegisterProvider(ManagedFileRepositoryProvider provider);
	
	public ManagedFileRepository newRepository(URI uri) throws RepositoryCreationException;
	
	public ManagedFileRepository newRepository(URI uri, Map<String, Object> params) throws RepositoryCreationException;
	
	public ManagedFileRepository newRepository(ManagedFilePath path) throws RepositoryCreationException;
	
	public ManagedFileRepository newRepository(ManagedFilePath path, Map<String, Object> params) throws RepositoryCreationException;


}

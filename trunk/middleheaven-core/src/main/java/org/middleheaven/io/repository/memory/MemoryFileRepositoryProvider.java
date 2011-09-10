/**
 * 
 */
package org.middleheaven.io.repository.memory;

import java.net.URI;
import java.util.Map;

import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.RepositoryCreationException;
import org.middleheaven.io.repository.engines.FileRepositoryProvider;

/**
 * 
 */
public class MemoryFileRepositoryProvider implements FileRepositoryProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepository newRepository(URI uri , Map<String, Object> params) throws RepositoryCreationException {
		if (!uri.getScheme().equalsIgnoreCase(this.getScheme())){
			throw new RepositoryCreationException("Unsupported Scheme " + uri.getScheme());
		}
		return new MemoryManagedFileRepository();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getScheme() {
		return "memory";
	}

}

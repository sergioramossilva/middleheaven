package org.middleheaven.io.repository;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.util.collections.EnhancedCollection;

public interface QueryableRepository extends ManagedFileRepository{

	/**
	 * Must return true, to correctly implement this interface.
	 */
	public boolean isQueriable();
	public EnhancedCollection<ManagedFile> listFiles() throws ManagedIOException;
	
}

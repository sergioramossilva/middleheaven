package org.middleheaven.io.repository;

import java.util.Collection;

import org.middleheaven.io.ManagedIOException;

public interface QueryableRepository {

	public boolean isQueriable();
	public Collection<? extends ManagedFile> listFiles() throws ManagedIOException;
	public Collection<? extends ManagedFile> listFiles(ManagedFileFilter filter) throws ManagedIOException; 
}

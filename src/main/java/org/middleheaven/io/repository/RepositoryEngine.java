package org.middleheaven.io.repository;

public interface RepositoryEngine  {

	
	public ManagedFileResolver getManagedFileResolver() throws RepositoryCreationException;

}

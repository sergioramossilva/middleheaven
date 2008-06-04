/*
 * Created on 2006/11/02
 *
 */
package org.middleheaven.io.repository.service;

import java.util.Set;

import org.middleheaven.io.repository.ManagedFile;


public interface FileRepositoryService {
	
	
	public Set<String> getRepositoriesNames();
    /**
     * Retrieve <code>ManagedFileRepository</code> registered to the name
     * or <code>null</code> if the name isn`t found.
     * @param name the registered name for the repository
     * @return <code>ManagedFileRepository</code> registered to the name
     * or <code>null</code> if the name isn`t found.
     */
    public ManagedFile getRepository(CharSequence name);

    public void registerRepository(CharSequence name, ManagedFile repository);
    public void unregisterRepository(CharSequence name);
}

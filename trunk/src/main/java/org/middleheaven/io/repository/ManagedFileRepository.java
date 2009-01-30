/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.io.repository;

import org.middleheaven.io.ManagedIOException;


public interface ManagedFileRepository{

    /**
     * 
     * @return logic value indicating if this repository supports reading capability
     */
    public abstract boolean isReadable();
    
    /**
     * 
     * @return logic value indicating if this repository supports writing capability
     */
    public abstract boolean isWriteable();

    /**
     * logic value indicating if this repository supports query capability.
     * If it does , it must implement <code>QueriableRepository</code>
     * @return logic value indicating if this repository supports query capability
     */
    public abstract boolean isQueriable();
    
    /**
     * Determines if a file exists in the repository
     * @param filename the file name. Example: config.properties, config.xml
     * @return
     * @throws FileStorageException
     */
    public abstract boolean exists(String filename) throws ManagedIOException;
    
    /**
     * Deletes the file from the repository
     * @param filename the file name. Example: config.properties, config.xml
     * @return
     * @throws FileStorageException
     */
    public abstract boolean delete(String filename) throws ManagedIOException;

    /**
     * 
     * @param filename
     * @return
     * @throws ManagedIOException
     */
    public abstract boolean delete(ManagedFile file) throws ManagedIOException;


    /**
     * Retrives a ManagedFile representing the file with the specified name
     * @param filename o nome do arquivo (apenas o nome do arquivo e a extensão)
     * @return 
     */
    public abstract ManagedFile retrive(String filename) throws ManagedIOException;


    /**
     * If the file already exists in the repository  do nothing. Else, copy the file to the repository
     * @param file to be storead
     * @throws RepositoryNotWritableException
     * @throws ManagedIOException
     */
    public abstract void store(ManagedFile file) throws RepositoryNotWritableException,ManagedIOException;

    
}

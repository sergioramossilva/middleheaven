/**
 * 
 */
package org.middleheaven.io.repository;

import org.middleheaven.io.ManagedIOException;

/**
 * 
 */
public interface ModificationTracableManagedFile extends ManagedFile {

	/**
	 * The time, in milisecounds from the reference epoc this file was last modified
	 * 
	 * @return The time, in milisecounds from the reference epoc this file was last modified
	 * 
	 */
	public long lastModified();
	
	/**
	 * changes the lastModified timestamp on this file.
	 * 
	 * @param timeMillis the new timestamp
	 */
	public void setLastModified(long timeMillis);
	
	
	/**
	 * 
	 * {@inheritDoc}
	 */
    public abstract ModificationTracableManagedFile retrive(String path) throws ManagedIOException;
    
	 /**
	  * 
	  * {@inheritDoc}
	  */
    public abstract ModificationTracableManagedFile retrive(ManagedFilePath path) throws ManagedIOException;
    

    /**
     * 
     * {@inheritDoc}
     */
	public ModificationTracableManagedFile createFile() throws UnsupportedOperationException;
    
	/**
	 * 
	 * {@inheritDoc}
	 */
	public ModificationTracableManagedFile createFolder() throws UnsupportedOperationException;



	
	
}

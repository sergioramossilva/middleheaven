/**
 * 
 */
package org.middleheaven.io.repository;

import org.middleheaven.io.ManagedIOException;

/**
 * 
 */
public interface ContentSource {

	
    /**
     * 
     * @return a handler for the file content
     */
    public ManagedFileContent getContent();
    
    /**
     * Copy this contents to another source's contents.
     * @param other
     * @throws ManagedIOException
     */
    public void copyTo(ContentSource other) throws ManagedIOException;
}

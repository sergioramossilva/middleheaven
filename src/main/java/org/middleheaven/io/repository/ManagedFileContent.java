package org.middleheaven.io.repository;

import java.io.InputStream;
import java.io.OutputStream;

import org.middleheaven.io.ManagedIOException;

public interface ManagedFileContent {

	
	public InputStream getInputStream() throws ManagedIOException;
	
	public OutputStream getOutputStream() throws ManagedIOException;
	
	
    /**
     * Returns the length of the content in bytes
     * @return
     * @throws ManagedIOException
     */
    public long getSize() throws ManagedIOException ;
    
}

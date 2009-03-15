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
    
    /**
     * Allow for establishing a start buffer size for the file contents.
     * This method is optional. Classes that do no support it must fail 
     * silently , i.e. do nothing
     * 
     * @param size
     * @throws ManagedIOException
     */
    public void setSize(long size) throws ManagedIOException ;
    
    
}

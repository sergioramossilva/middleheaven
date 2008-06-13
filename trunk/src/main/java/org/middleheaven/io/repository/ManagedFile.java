/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.io.repository;

import java.net.URL;
import java.util.Collection;

import org.middleheaven.io.ManagedIOException;


/**
 * @author  Sergio M.M. Taborda
 */
public interface ManagedFile extends ManagedFileResolver{

	public Collection<? extends ManagedFile> listFiles() throws ManagedIOException;

	public Collection<? extends  ManagedFile> listFiles(ManagedFileFilter filter) throws ManagedIOException;
	
	/**
	 * Test if the another file is contained in this file.
	 * the file is contained if <code>this</code> file is the parent of <code>other</code>
	 * @param other
	 * @return
	 */
	public boolean contains(ManagedFile other);
	
	/**
	 *  Determines if this file exists.
	 */
	public boolean exists();
	
	/**
	 * 
	 * @return <code>true</code> if this implements <code>WatchableRepository</code> and a <code>FileChangeListener</code> can be attached to this in order to listen to
	 * changes in the files container in this file.<code>false</code> otherwise.
	 */
	public boolean isWatchable();
	
	public ManagedFile getParent();
	
	public ManagedFileType getType();
	
	public URL getURL();
	

    public String getName();
    
    /**
     * indicates if getInputStream() can be invoked
     * @return
     */
    public boolean isReadable();
    
    /**
     * indicates if getOutputStream() can be invoked
     * @return
     */
    public boolean isWriteable();
    
    /**
     * 
     * @return a handler for the file content
     */
    public ManagedFileContent getContent();
    

    /**
     * Copies the content of this file to another file
     * @param other
     * @throws ManagedIOException
     */
    public void copyTo(ManagedFile other) throws ManagedIOException ;

    /**
     * Delete this file
     */
	public boolean delete();

	/**
	 * Create a file on the url pointed by this file
	 */
	public void createFile();
    
	/**
	 * Create a folder on the url pointed by this file
	 */
	public void createFolder();
    
}

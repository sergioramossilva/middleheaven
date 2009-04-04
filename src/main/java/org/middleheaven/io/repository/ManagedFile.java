/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.io.repository;

import java.net.URL;
import java.util.Collection;

import org.middleheaven.io.ManagedIOException;


/**
 * A common abstract for all types of files : disk files, url addresses, uploaded files, email attachments, etc ...
 */
public interface ManagedFile extends ManagedFileResolver{

	public Collection<? extends ManagedFile> listFiles() throws ManagedIOException;

	public Collection<? extends  ManagedFile> listFiles(ManagedFileFilter filter) throws ManagedIOException;
	
	
	public void setName(String name);
	
	/**
	 * For FILE managed files it returns the same as <code>getContent().getSize</code>
	 * for FOLDER managed files it returns the sum of inner FILE and FOLDER getSize()
	 * 
	 * @return the byte length of the managed file.   
	 * @throws ManagedIOException
	 */
	public long getSize() throws ManagedIOException;
	
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
	 * If {@ this} is of type {@link ManagedFileType#VIRTUAL} create and return a {@ ManagedFile} file on the URL pointed by {@code this}.
	 * The new file substitutes {@code this} on its parent.
	 * If {@ this} is not of type {@link ManagedFileType#VIRTUAL} and {@ this} is of type {@link ManagedFileType#FILE} {@ this } is returned.
	 * Else an {@link UnsupportedOperationException} is thrown;
	 * @return the created file
	 * @throws UnsupportedOperationException if {@ this} is not of VIRTUAL or FILE type, or this operation is not implemented on the target class.
	 */
	public ManagedFile createFile() throws UnsupportedOperationException;
    
	/**
	 * If {@ this} is of type {@link ManagedFileType#VIRTUAL} create and return a {@ ManagedFile} folder on the URL pointed by {@code this}.
	 * The new file substitutes {@code this} on its parent.
	 * If {@ this} is not of type {@link ManagedFileType#VIRTUAL} and {@ this} is of type {@link ManagedFileType#FOLDER} {@ this } is returned.
	 * Else an {@link UnsupportedOperationException} is thrown;
	 * @return the created folder
	 * @throws UnsupportedOperationException if {@ this} is not of VIRTUAL or FOLDER type, or this operation is not implemented on the target class.
	 */
	public ManagedFile createFolder() throws UnsupportedOperationException;
    
}

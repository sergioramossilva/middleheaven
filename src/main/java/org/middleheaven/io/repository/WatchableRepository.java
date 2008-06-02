/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.io.repository;

public interface WatchableRepository{

    /**
     * Registers a <code>FileChangeListener</code> on this repository.
     * The <code>FileChangeListener</code> will be informed of changes in the file.
     * 
     * @param listener the <code>FileChangeListener</code> instance
     * @param file the file to monitor for changes
     */
    public void addFileChangelistener(FileChangeListener listener, ManagedFile file);
    
    public void removeFileChangelistener(FileChangeListener listener, ManagedFile file);
}

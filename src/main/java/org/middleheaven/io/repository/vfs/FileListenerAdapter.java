/**
 * 
 */
package org.middleheaven.io.repository.vfs;

import org.apache.commons.vfs.FileChangeEvent;
import org.apache.commons.vfs.FileListener;
import org.middleheaven.io.repository.FileChangeListener;

class FileListenerAdapter implements FileListener {

	private FileChangeListener listener;
	public FileListenerAdapter(FileChangeListener listener){
		this.listener = listener;
	}

	@Override
	public void fileChanged(FileChangeEvent event) throws Exception {
		this.listener.onChange(
				new org.middleheaven.io.repository.FileChangeEvent(
						new VirtualFileSystemManagedFile(event.getFile()
						)
				)
		);
	}

	@Override
	public void fileCreated(FileChangeEvent event) throws Exception {
		this.listener.onChange(
				new org.middleheaven.io.repository.FileChangeEvent(
						new VirtualFileSystemManagedFile(event.getFile()
						)
				)
		);
	}

	@Override
	public void fileDeleted(FileChangeEvent event) throws Exception {
		this.listener.onChange(
				new org.middleheaven.io.repository.FileChangeEvent(
						new VirtualFileSystemManagedFile(event.getFile()
						)
				)
		);
	}

}
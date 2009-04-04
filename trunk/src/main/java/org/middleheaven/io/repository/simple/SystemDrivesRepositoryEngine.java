package org.middleheaven.io.repository.simple;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileResolver;
import org.middleheaven.io.repository.RepositoryCreationException;
import org.middleheaven.io.repository.RepositoryEngine;
import org.middleheaven.io.repository.vfs.VirtualFileSystemManagedFile;

public class SystemDrivesRepositoryEngine implements RepositoryEngine {


	@Override
	public ManagedFileResolver getManagedFileResolver() throws RepositoryCreationException {
		return new ManagedFileResolver(){

			@Override
			public ManagedFile resolveFile(String filepath) {
				try {
					FileObject file = VFS.getManager().resolveFile(filepath);
					
					return new VirtualFileSystemManagedFile(file);
				} catch (FileSystemException e) {
					throw new RepositoryCreationException(e);
				}
			}
			
		};
	}

}

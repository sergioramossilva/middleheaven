package org.middleheaven.io.repository.vfs;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileResolver;
import org.middleheaven.io.repository.RepositoryCreationException;
import org.middleheaven.io.repository.RepositoryEngine;

public class CommonsVSFRepositoryEngine implements RepositoryEngine {

	private CommonsVFSManagedFileResolver resolver = new CommonsVFSManagedFileResolver();


	@Override
	public ManagedFileResolver getManagedFileResolver() throws RepositoryCreationException {
		return resolver;
	}

	
	private class CommonsVFSManagedFileResolver implements ManagedFileResolver{

		@Override
		public ManagedFile resolveFile(String filepath) {
			try {
				FileObject file = VFS.getManager().resolveFile(filepath);
				
				return new VirtualFileSystemMangedFile(file);
			} catch (FileSystemException e) {
				throw new RepositoryCreationException(e);
			}
		}
		
	}
}

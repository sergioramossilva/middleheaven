package org.middleheaven.io.repository.vfs;

import java.net.URI;
import java.util.Map;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.RepositoryCreationException;
import org.middleheaven.io.repository.engines.FileRepositoryProvider;

// TODO change to an activator
public class CommonsVSFRepositoryProvider implements FileRepositoryProvider {

	private VirtualFileWatchService virtualFileWatchService = new VirtualFileWatchService();

	public CommonsVSFRepositoryProvider (){}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepository newRepository(URI uri ,Map<String, Object> params)
			throws RepositoryCreationException {
		try {
			FileObject file = VFS.getManager().resolveFile(uri.toString());
			
			return null; //new VirtualFileSystemManagedFile(virtualFileWatchService, file);
		} catch (FileSystemException e) {
			throw new RepositoryCreationException(e);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getScheme() {
		// TODO Auto-generated method stub
		return ""; //VFS.getManager().getSchemes()[0];
	}
}

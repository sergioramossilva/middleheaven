package org.middleheaven.io.repository;


import java.net.URI;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.watch.Watchable;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;

/**
 * An abstract implementation of a {@link MediaManagedFile}.
 */
public abstract class AbstractMediaManagedFile extends AbstractManagedFile implements MediaManagedFile {


	protected AbstractMediaManagedFile(ManagedFileRepository repositoy ) {
		super(repositoy);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileType getType() {
		return ManagedFileType.FILE;
	}
	
	@Override
	public boolean delete() {
		return false;
	}

	@Override
	public boolean exists() {
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getSize() throws ManagedIOException {
		return this.getContent().getSize();
	}
	
	@Override
	public ManagedFile doCreateFile() {
		throw new UnsupportedOperationException("File creation is not supported");
	}

	@Override
	public ManagedFile doCreateFolder() {
		throw new UnsupportedOperationException("Folder creation is not supported");
	}
	
	@Override
	public EnhancedCollection<ManagedFile> children() throws ManagedIOException {
		return CollectionUtils.emptyCollection();
	}

	
	@Override
	public URI getURI() {
		return null;
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isWatchable() {
		return false;
	}

	@Override
	public boolean isWriteable() {
		return false;
	}

}

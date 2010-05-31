package org.middleheaven.io.repository;

import java.net.URL;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedCollection;


public abstract class AbstractContentManagedFile extends AbstractManagedFile {

	protected AbstractContentManagedFile(ManagedFilePath path) {
		super(path);
	}

	/**
	 * @return <code>false</code> as this file is not a directory
	 */
	@Override
	public final boolean contains(ManagedFile other) {
		return false;
	}
	
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
	public URL getURL() {
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

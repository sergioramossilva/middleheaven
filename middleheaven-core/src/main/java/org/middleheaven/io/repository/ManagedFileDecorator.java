/**
 * 
 */
package org.middleheaven.io.repository;

import java.net.URI;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.watch.WatchEventChannel;
import org.middleheaven.io.repository.watch.WatchService;
import org.middleheaven.io.repository.watch.WatchEvent.Kind;
import org.middleheaven.util.collections.Enumerable;
import org.middleheaven.util.collections.Walker;

/**
 * 
 */
class ManagedFileDecorator implements ManagedFile {

	private final ManagedFile original;

	public ManagedFileDecorator ( ManagedFile original){
		this.original = original;
	}

	/**
	 * {@inheritDoc}
	 */
	public void each(Walker<ManagedFile> walker) {
		original.each(walker);
	}

	/**
	 * {@inheritDoc}
	 */
	public void eachRecursive(Walker<ManagedFile> walker) {
		original.eachRecursive(walker);
	}

	/**
	 * {@inheritDoc}
	 */
	public void eachParent(Walker<ManagedFile> walker) {
		original.eachParent(walker);
	}

	/**
	 * {@inheritDoc}
	 */
	public WatchEventChannel register(WatchService watchService, Kind... events) {
		return original.register(watchService, events);
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFileRepository getRepository() {
		return original.getRepository();
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFilePath getPath() {
		return original.getPath();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canRenameTo(String newBaseName) {
		return original.canRenameTo(newBaseName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void renameTo(String newBaseName) throws ManagedIOException {
		original.renameTo(newBaseName);
	}

	/**
	 * {@inheritDoc}
	 */
	public long getSize() throws ManagedIOException {
		return original.getSize();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean contains(ManagedFile other) {
		return original.contains(other);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean exists() {
		return original.exists();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isWatchable() {
		return original.isWatchable();
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFile getParent() {
		return original.getParent();
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFileType getType() {
		return original.getType();
	}

	/**
	 * {@inheritDoc}
	 */
	public URI getURI() {
		return original.getURI();
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFile retrive(String path) throws ManagedIOException {
		return original.retrive(path);
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
		return original.retrive(path);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isReadable() {
		return original.isReadable();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isWriteable() {
		return original.isWriteable();
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFileContent getContent() {
		return original.getContent();
	}

	/**
	 * {@inheritDoc}
	 */
	public void copyTo(ManagedFile other) throws ManagedIOException {
		original.copyTo(other);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean delete() {
		return original.delete();
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFile createFile() throws UnsupportedOperationException {
		return original.createFile();
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagedFile createFolder() throws UnsupportedOperationException {
		return original.createFolder();
	}

	/**
	 * {@inheritDoc}
	 */
	public Enumerable<ManagedFile> children() {
		return original.children();
	}
	
	
	
}

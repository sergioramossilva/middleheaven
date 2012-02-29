/**
 * 
 */
package org.middleheaven.io.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.watch.WatchEventChannel;
import org.middleheaven.io.repository.watch.WatchService;
import org.middleheaven.io.repository.watch.WatchEvent.Kind;
import org.middleheaven.io.repository.watch.Watchable;
import org.middleheaven.util.collections.Enumerable;
import org.middleheaven.util.collections.Walker;

/**
 * {@link ManagedFile} decorator that provides text specific methods. Useful to handle text files.
 */
public class TextFile implements ManagedFile {

	private final ManagedFile original;

	/**
	 * Constructor.
	 * @param file
	 */
	public TextFile(ManagedFile original) {
		this.original = original;
	}

	public void each(Walker<ManagedFile> walker) {
		original.each(walker);
	}

	public void eachRecursive(Walker<ManagedFile> walker) {
		original.eachRecursive(walker);
	}

	public void eachParent(Walker<ManagedFile> walker) {
		original.eachParent(walker);
	}

	public WatchEventChannel register(WatchService watchService, Kind... events) {
		return original.register(watchService, events);
	}

	public ManagedFileRepository getRepository() {
		return original.getRepository();
	}

	public ManagedFilePath getPath() {
		return original.getPath();
	}

	public boolean canRenameTo(String newBaseName) {
		return original.canRenameTo(newBaseName);
	}

	public void renameTo(String newBaseName) throws ManagedIOException {
		original.renameTo(newBaseName);
	}

	public long getSize() throws ManagedIOException {
		return original.getSize();
	}

	public boolean contains(ManagedFile other) {
		return original.contains(other);
	}

	public boolean exists() {
		return original.exists();
	}

	public boolean isWatchable() {
		return original.isWatchable();
	}

	public ManagedFile getParent() {
		return original.getParent();
	}

	public ManagedFileType getType() {
		return original.getType();
	}

	public URI getURI() {
		return original.getURI();
	}

	public ManagedFile retrive(String path) throws ManagedIOException {
		return original.retrive(path);
	}

	public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
		return original.retrive(path);
	}

	public boolean isReadable() {
		return original.isReadable();
	}

	public boolean isWriteable() {
		return original.isWriteable();
	}

	public ManagedFileContent getContent() {
		return original.getContent();
	}

	public void copyTo(ManagedFile other) throws ManagedIOException {
		original.copyTo(other);
	}

	public boolean delete() {
		return original.delete();
	}

	public ManagedFile createFile() throws UnsupportedOperationException {
		return original.createFile();
	}

	public ManagedFile createFolder() throws UnsupportedOperationException {
		return original.createFolder();
	}

	public CharSequence getText(){
		BufferedReader reader = null;
		try{

			reader = new BufferedReader(new InputStreamReader(this.getContent().getInputStream()));

			StringBuilder builder = new StringBuilder();

			String line;
			while ((line = reader.readLine()) !=null){
				builder.append(line).append("\n");
			}

			return builder;


		} catch (IOException e){
			throw ManagedIOException.manage(e);
		} finally {
			if (reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					throw ManagedIOException.manage(e);
				}
			}
		}
	}
	
	public void eachLine (Walker<String> walker){
		BufferedReader reader = null;
		try{

			reader = new BufferedReader(new InputStreamReader(this.getContent().getInputStream()));

		
			String line;
			while ((line = reader.readLine()) !=null){
				walker.doWith(line);
			}

		} catch (IOException e){
			throw ManagedIOException.manage(e);
		} finally {
			if (reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					throw ManagedIOException.manage(e);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<ManagedFile> children() {
		return original.children();
	}

	
}

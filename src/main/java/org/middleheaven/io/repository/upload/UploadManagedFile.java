package org.middleheaven.io.repository.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.fileupload.FileItem;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.AbstractManagedFile;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileContent;
import org.middleheaven.io.repository.ManagedFileFilter;
import org.middleheaven.io.repository.ManagedFileType;

public class UploadManagedFile extends AbstractManagedFile  {

	private final FileItem fileItem;
	private final ManagedFile parent;
	
	UploadManagedFile(FileItem fileItem, ManagedFile parent){
		this.fileItem = fileItem;
		this.parent = parent;
	}

	public boolean equals(Object other){
		return other instanceof UploadManagedFile && equals((UploadManagedFile)other);
	}
	
	public boolean equals(UploadManagedFile other){
		return other.fileItem.equals(this.fileItem);
	}
	
	public int hashCode(){
		return this.fileItem.hashCode();
	}

	/**
	 * @return <code>false</code> as this file is not a directory
	 */
	@Override
	public boolean contains(ManagedFile other) {
		return false;
	}

	@Override
	public void createFile() {
		throw new UnsupportedOperationException("File creation is not supported");
	}

	@Override
	public void createFolder() {
		throw new UnsupportedOperationException("Folder creation is not supported");
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
	public ManagedFileContent getContent() {
		return new FileItemManagedFileContent();
	}

	@Override
	public String getName() {
		return fileItem.getFieldName();
	}

	@Override
	public ManagedFile getParent() {
		return parent;
	}

	@Override
	public ManagedFileType getType() {
		return ManagedFileType.VIRTUAL;
	}

	@Override
	public URL getURL() {
		try {
			return new URL(fileItem.getFieldName());
		} catch (MalformedURLException e) {
			throw new ManagedIOException(e);
		}
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isWriteable() {
		return false;
	}

	@Override
	public Collection<? extends ManagedFile> listFiles()
	throws ManagedIOException {
		return Collections.emptySet();
	}

	@Override
	public Collection<? extends ManagedFile> listFiles(ManagedFileFilter filter)
	throws ManagedIOException {
		return Collections.emptySet();
	}

	@Override
	public ManagedFile resolveFile(String filepath) {
		return null;
	}

	private class FileItemManagedFileContent implements  ManagedFileContent{

		@Override
		public InputStream getInputStream() throws ManagedIOException{

			try {
				return fileItem.getInputStream();
			} catch (IOException e) {
				throw ManagedIOException.manage(e);
			}

		}

		@Override
		public OutputStream getOutputStream() throws ManagedIOException{
			try {
				return fileItem.getOutputStream();
			} catch (IOException e) {
				throw ManagedIOException.manage(e);
			}
		}

		@Override
		public long getSize() throws ManagedIOException {
			return fileItem.getSize();
		}

	}
}

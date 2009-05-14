package org.middleheaven.io.repository.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.fileupload.FileItem;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.AbstractContentManagedFile;
import org.middleheaven.io.repository.AbstractManagedFile;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileType;
import org.middleheaven.io.repository.MediaManagedFile;
import org.middleheaven.io.repository.MediaManagedFileContent;

public class UploadManagedFile extends AbstractContentManagedFile implements MediaManagedFile {

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

	@Override
	public boolean delete() {
		fileItem.delete();
		return true;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public MediaManagedFileContent getContent() {
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
	public URL getURL() {
		try {
			return new URL(fileItem.getFieldName());
		} catch (MalformedURLException e) {
			throw new ManagedIOException(e);
		}
	}


	@Override
	public ManagedFile resolveFile(String filepath) {
		return null;
	}

	private class FileItemManagedFileContent implements  MediaManagedFileContent{

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

		@Override
		public boolean setSize(long size) throws ManagedIOException {
			return false;
		}

		@Override
		public String getContentType() {
			return fileItem.getContentType();
		}

		@Override
		public void setContentType(String contentType) {
			// not supported. fail silently
		}

	}

	@Override
	public long getSize() throws ManagedIOException {
		return fileItem.getSize();
	}

	@Override
	public void setName(String name) {
		throw new ManagedIOException("unsuppported operation");
	}


}

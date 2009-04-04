package org.middleheaven.io.repository;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

import org.middleheaven.io.ByteBuffer;
import org.middleheaven.io.ManagedIOException;

public class BufferedMediaVirtualFile extends AbstractManagedFile implements MediaManagedFile {

	ByteBuffer buffer = new ByteBuffer();
	
	String contentType = "octet-stream";

	private String name;
	
	public BufferedMediaVirtualFile(){
		
	}
	
	@Override
	public MediaManagedFileContent getContent() {
		return new MediaManagedFileContent(){

			@Override
			public String getContentType() {
				return contentType;
			}

			@Override
			public void setContentType(String type) {
				contentType = type;
			}

			@Override
			public InputStream getInputStream() throws ManagedIOException {
				return buffer.getInputStream();
			}

			@Override
			public OutputStream getOutputStream() throws ManagedIOException {
				return buffer.getOutputStream();
			}

			@Override
			public long getSize() throws ManagedIOException {
				return buffer.getSize();
			}

			@Override
			public boolean setSize(long size) throws ManagedIOException {
				return buffer.setSize((int)size);
			}
			
		};
	}

	@Override
	public boolean contains(ManagedFile other) {
		return false;
	}

	@Override
	public ManagedFile doCreateFile() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ManagedFile doCreateFolder() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean delete() {
		return false;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ManagedFile getParent() {
		return null;
	}

	@Override
	public long getSize() throws ManagedIOException {
		return buffer.getSize();
	}

	@Override
	public ManagedFileType getType() {
		return ManagedFileType.VIRTUAL;
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
		return true;
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

	@Override
	public void setName(String name) {
		this.name = name;
	}

}

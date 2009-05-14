package org.middleheaven.io.repository;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.middleheaven.io.ByteBuffer;
import org.middleheaven.io.ManagedIOException;

public class BufferedMediaVirtualFile extends AbstractContentManagedFile implements MediaManagedFile {

	ByteBuffer buffer = new ByteBuffer();
	
	String contentType = "octet-stream";

	private String name;

	
	public BufferedMediaVirtualFile(String name){
		this.name = name;
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
	public ManagedFile resolveFile(String filepath) {
		return null;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}



}

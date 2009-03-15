package org.middleheaven.io.repository;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

import org.middleheaven.io.ManagedIOException;

public abstract class StreamBasedManagedFile extends AbstractManagedFile implements MediaManagedFile {


	private String name;
	private ManagedFile parent;
	private String contentType = "octet-stream";
	
	public StreamBasedManagedFile(String name, ManagedFile parent) {
		super();
		this.name = name;
		this.parent = parent;
	}
	
	protected String getContentType(){
		return contentType;
	}
	protected void setContentType(String type){
		this.contentType = type;
	}
	
	@Override
	public MediaManagedFileContent getContent(){
		return new MediaManagedFileContent(){

			@Override
			public String getContentType() {
				return StreamBasedManagedFile.this.getContentType();
			}

			@Override
			public void setContentType(String contentType) {
				StreamBasedManagedFile.this.setContentType(contentType);
			}

			@Override
			public InputStream getInputStream() throws ManagedIOException {
				return StreamBasedManagedFile.this.getInputStream();
			}

			@Override
			public OutputStream getOutputStream() throws ManagedIOException {
				return StreamBasedManagedFile.this.getOutputStream();
			}

			@Override
			public long getSize() throws ManagedIOException {
				return StreamBasedManagedFile.this.getSize();
			}

			@Override
			public void setSize(long size) throws ManagedIOException {
				StreamBasedManagedFile.this.setSize(size);
			}
			
		};
		
	}
	
	protected abstract InputStream getInputStream() throws ManagedIOException;

	protected abstract OutputStream getOutputStream() throws ManagedIOException;
	
	protected abstract void setSize(long size);
	
	@Override
	public abstract long getSize();
	
	@Override
	public boolean contains(ManagedFile other) {
		return false;
	}

	@Override
	public void createFile() {
		// no-op
	}

	@Override
	public void createFolder() {
		// no-op
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
	public String getName() {
		return name;
	}

	@Override
	public ManagedFile getParent() {
		return parent;
	}

	@Override
	public ManagedFileType getType() {
		return ManagedFileType.FILE;
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
	public Collection<? extends ManagedFile> listFiles() throws ManagedIOException {
		return Collections.emptySet();
	}

	@Override
	public Collection<? extends ManagedFile> listFiles(ManagedFileFilter filter) throws ManagedIOException {
		return Collections.emptySet();
	}

	@Override
	public ManagedFile resolveFile(String filepath) {
		return null;
	}


}

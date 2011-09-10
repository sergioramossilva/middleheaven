package org.middleheaven.io.repository;

import java.io.InputStream;
import java.io.OutputStream;

import org.middleheaven.io.ManagedIOException;

public abstract class StreamBasedManagedFile extends AbstractContentManagedFile implements MediaManagedFile {


	private String name;
	private ManagedFile parent;
	private String contentType = "octet-stream";
	
	public StreamBasedManagedFile(String name, ManagedFile parent) {
		super(new SimpleManagedFilePath(parent.getPath() , name));
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
			public boolean setSize(long size) throws ManagedIOException {
				return StreamBasedManagedFile.this.setSize(size);
			}
			
		};
		
	}
	
	protected abstract InputStream getInputStream() throws ManagedIOException;

	protected abstract OutputStream getOutputStream() throws ManagedIOException;
	
	protected abstract boolean setSize(long size);
	
	@Override
	public abstract long getSize();
	

	@Override
	public ManagedFile doCreateFile() {
		return this;
	}

	@Override
	public ManagedFile doCreateFolder() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void renameTo(String name) {
		this.setPath(new SimpleManagedFilePath(parent.getPath() , name));
	}
	
	@Override
	public ManagedFile getParent() {
		return parent;
	}

	public ManagedFile retrive(String filename){
		return new UnexistantManagedFile(this,filename); 
	}

}

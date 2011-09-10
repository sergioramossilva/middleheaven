package org.middleheaven.io.repository;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.middleheaven.io.ManagedIOException;

public class CharSequenceManagedFile extends AbstractContentManagedFile implements MediaManagedFile {

	private CharSequence body;
	private String name;
	private String contentType;

	public CharSequenceManagedFile(CharSequence body, String name, String contentType){
		super (new SimpleManagedFilePath("/".concat(name)));
		this.body = body;
		this.name = name;
		this.contentType = contentType;
	}
	

	
	public String getText(){
		return body.toString();
	}
	
	@Override
	public MediaManagedFileContent getContent() {
		return new MediaManagedFileContent(){

			@Override
			public String getContentType() {
				return contentType;
			}

			@Override
			public void setContentType(String newContentType) {
				contentType = newContentType;
			}

			@Override
			public InputStream getInputStream() throws ManagedIOException {
				return new ByteArrayInputStream(body.toString().getBytes());
			}

			@Override
			public OutputStream getOutputStream() throws ManagedIOException {
				throw new UnsupportedOperationException();
			}

			@Override
			public long getSize() throws ManagedIOException {
				return body.toString().getBytes().length;
			}

			@Override
			public boolean setSize(long size) throws ManagedIOException {
				// not supported
				return false;
			}
			
		};
	}

	@Override
	public ManagedFile getParent() {
		return null;
	}

	@Override
	public long getSize() throws ManagedIOException {
		return body.toString().getBytes().length;
	}



	@Override
	public void renameTo(String name) {
		this.name = name;
	}

	@Override
	public ManagedFile retrive(String filepath) {
		return new UnexistantManagedFile(this,filepath);
	}



	
}

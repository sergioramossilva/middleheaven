package org.middleheaven.io.repository;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

import org.middleheaven.io.ManagedIOException;

public class CharSequenceManagedFile extends AbstractManagedFile implements MediaManagedFile {

	private CharSequence body;
	private String name;
	private String contentType;

	public CharSequenceManagedFile(CharSequence body, String name, String contentType){
		this.body = body;
		this.name = name;
		this.contentType = contentType;
	}
	
	@Override
	protected ManagedFile doCreateFile() {
		return null;
	}

	@Override
	protected ManagedFile doCreateFolder() {
		return null;
	}

	@Override
	public boolean contains(ManagedFile other) {
		return false;
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
	public String getName() {
		return name;
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
		return false;
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
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public ManagedFile resolveFile(String filepath) {
		return new VoidManagedFile(this,filepath);
	}

	
}

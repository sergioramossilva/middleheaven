package org.middleheaven.mail.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

import org.middleheaven.io.repository.MediaManagedFile;

class ManagedFileDataSource implements DataSource {

	private MediaManagedFile file;

	ManagedFileDataSource(MediaManagedFile file) {
		this.file = file;
	}
	
	@Override
	public String getContentType() {
		return file.getContent().getContentType();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return file.getContent().getInputStream();
	}

	@Override
	public String getName() {
		return file.getPath().getFileName();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return file.getContent().getOutputStream();
	}

}

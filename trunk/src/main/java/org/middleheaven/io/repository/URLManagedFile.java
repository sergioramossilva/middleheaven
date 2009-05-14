package org.middleheaven.io.repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.middleheaven.io.ManagedIOException;

public class URLManagedFile extends StreamBasedManagedFile {

	private URL url;

	public URLManagedFile(URL url) {
		super(url.getFile(), null);
		this.url = url;
	}

	
	public URL getURL(){
		return url;
	}
	
	@Override
	protected InputStream getInputStream() throws ManagedIOException {
		try {
			return url.openStream();
		} catch (IOException e) {
			throw ManagedIOException.manage(e);
		}
	}

	@Override
	protected OutputStream getOutputStream() throws ManagedIOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getSize() {
		// TODO implement URLManagedFile.getSize
		return 0;
	}

	@Override
	protected boolean setSize(long size) {
		return false;
	}

	@Override
	public void setName(String name) {
		//no-op
	}

}

package org.middleheaven.io.repository;

import java.io.InputStream;
import java.io.OutputStream;

import org.middleheaven.io.ByteBuffer;
import org.middleheaven.io.ManagedIOException;

/**
 * Uses a {@link ByteBuffer} to maintain information in memory.
 */
public class BufferedMediaManagedFileContent extends StreamBasedMediaManagedFileContent  {

	private ByteBuffer buffer = new ByteBuffer();

	public BufferedMediaManagedFileContent(){

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




}

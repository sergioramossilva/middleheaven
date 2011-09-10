package org.middleheaven.process.web.server.filters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;


public class GZipResponseStream extends ServletOutputStream {

	private ByteArrayOutputStream baos;
	private GZIPOutputStream gzipstream;
	private HttpServletResponse response;
	private ServletOutputStream output;

	private boolean closed = false;
	
	public GZipResponseStream(HttpServletResponse response) throws IOException {
		super();
		closed = false;
		this.response = response;
		this.output = response.getOutputStream();
		this.baos = new ByteArrayOutputStream();
		this.gzipstream = new GZIPOutputStream(baos);

	}

	public void close() throws IOException {
		if (closed) {
			throw new IOException("This output stream has already been closed");
		}

		gzipstream.finish();

		final byte[] bytes = baos.toByteArray();

		response.addHeader("Content-Length",  Integer.toString(bytes.length)); 
		response.addHeader("Content-Encoding", "gzip");
		output.write(bytes);
		output.flush();
		output.close();
		closed = true;
	}

	public void flush() throws IOException {
		if (closed) {
			throw new IOException("Cannot flush a closed output stream");
		}
		gzipstream.flush();
	}

	public void write(int b) throws IOException {
		if (closed) {
			throw new IOException("Cannot write to a closed output stream");
		}
		gzipstream.write((byte)b);
	}

	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}

	public void write(byte[] b, int off, int len) throws IOException {

		if (closed) {
			throw new IOException("Cannot write to a closed output stream");
		}

		gzipstream.write(b, off, len);
	}

	public boolean closed() {
		return this.closed;
	}

	public void reset() {
		//no-op
	}

	public int getLength() {
		return this.baos.toByteArray().length;
	}
}


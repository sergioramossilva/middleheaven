package org.middleheaven.process.web.server.filters;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class GZipResponseWrapper extends HttpServletResponseWrapper {
	protected HttpServletResponse origResponse;
	protected GZipResponseStream stream;
	protected PrintWriter writer;

	
	public GZipResponseWrapper(HttpServletResponse response) {
		super(response);
		origResponse = response;
	}

	public GZipResponseStream createOutputStream() throws IOException {
		return new GZipResponseStream(origResponse);
	}

	public void finishResponse() throws IOException {

		if (writer != null) {
			writer.close();
		} else if (stream != null) {
			stream.close();
		}

	}

	public void flushBuffer() throws IOException {
		stream.flush();
	}

	public ServletOutputStream getOutputStream() throws IOException {
		if (writer != null) {
			throw new IllegalStateException("getWriter() has already been called.");
		}

		if (stream == null){
			stream = createOutputStream();
		}

		return stream;
	}

	public PrintWriter getWriter() throws IOException {
		if (writer != null) {
			return writer;
		}

		if (stream == null){
			stream = createOutputStream();
		} else if (stream.getLength() > 0) {
			throw new IllegalStateException("getOutputStream() has already been called!");
		}	

		writer = new PrintWriter(new OutputStreamWriter(stream, this.getCharacterEncoding() ));
		return writer;
	}

	public void setContentLength(int length) {}
}
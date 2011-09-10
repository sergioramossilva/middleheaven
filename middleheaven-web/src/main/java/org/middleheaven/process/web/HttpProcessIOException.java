package org.middleheaven.process.web;

import java.io.IOException;


public class HttpProcessIOException extends HttpProcessException {

	private IOException ioe;

	public HttpProcessIOException(IOException cause) {
		super(cause);
		this.ioe = cause;
	}

	
	public IOException getIOException(){
		return ioe;
	}
}

package org.middleheaven.web.processing.action;

import java.io.IOException;

import org.middleheaven.web.processing.HttpProcessException;

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

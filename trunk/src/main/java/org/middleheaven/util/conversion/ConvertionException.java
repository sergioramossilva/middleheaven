package org.middleheaven.util.conversion;

public class ConvertionException extends RuntimeException {

	public ConvertionException(Exception e) {
		super(e);
	}

	public ConvertionException(String msg) {
		super(msg);
	}

}

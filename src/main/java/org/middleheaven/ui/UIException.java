package org.middleheaven.ui;


public class UIException extends RuntimeException {


	private static final long serialVersionUID = 5972335093227978855L;

	public UIException(Exception e) {
		super(e);
	}

	public UIException(String msg) {
		super(msg);
	}

}

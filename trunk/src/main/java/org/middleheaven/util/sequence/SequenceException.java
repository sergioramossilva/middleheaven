package org.middleheaven.util.sequence;

public class SequenceException extends RuntimeException {

	
	public SequenceException(Throwable cause){
		super(cause);
	}

	public SequenceException(String msg) {
		super(msg);
	}
}

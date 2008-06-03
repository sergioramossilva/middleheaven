package org.middleheaven.report;

public class ReportException extends RuntimeException {

	
	public ReportException(Throwable cause){
		super(cause);
	}

	protected ReportException(String msg) {
		super(msg);
	}
}

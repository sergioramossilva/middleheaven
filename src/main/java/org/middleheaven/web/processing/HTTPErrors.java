package org.middleheaven.web.processing;

public enum HTTPErrors {
	NOT_FOUND(400);

	
	
	private int errorCode;

	private HTTPErrors(int errorCode){
		this.errorCode = errorCode;
	}
	
	public int errorCode(){
		return errorCode;
	}
}

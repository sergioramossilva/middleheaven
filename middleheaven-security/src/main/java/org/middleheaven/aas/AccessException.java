package org.middleheaven.aas;

public class AccessException extends RuntimeException {

	
	public AccessException(){
		
	}
	
	public AccessException(String msg){
		super(msg);
	}
	
	public AccessException(Throwable cause){
		super(cause);
	}
}

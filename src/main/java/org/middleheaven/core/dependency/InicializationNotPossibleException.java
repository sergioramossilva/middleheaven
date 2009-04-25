package org.middleheaven.core.dependency;

public class InicializationNotPossibleException extends Exception {

	public InicializationNotPossibleException(RuntimeException e) {
		super(e);
	}
	
	public InicializationNotPossibleException() {
		super();
	}


}

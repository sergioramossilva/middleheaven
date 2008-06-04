package org.middleheaven.core.reflection;

public class ReflectionException extends RuntimeException {


	public ReflectionException(Throwable t){
		super(t);
	}

	public ReflectionException(String msg) {
		super(msg);
	}

	protected ReflectionException() {
	}
}

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

	public static RuntimeException manage(Throwable e) {
		if (e instanceof SecurityException){
			throw new IllegalAccessReflectionException(e);
		} else if (e instanceof NoSuchMethodException){
			throw new NoSuchMethodReflectionException(e);
		}
		
		return new RuntimeException(e);
	}
}

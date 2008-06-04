package org.middleheaven.core.exception;

import org.middleheaven.core.reflection.ReflectionUtils;

public abstract class ExceptionUtils {

	
	private ExceptionUtils(){}
	
	public RuntimeException toRuntimeException(Throwable t){
		if (t instanceof RuntimeException){
			return (RuntimeException)t;
		} else {
			return new RuntimeException(t);
		}
	}
	
	public RuntimeException toRuntimeException(Class<? extends RuntimeException> runtimeClass,Throwable t){
		if (t instanceof RuntimeException){
			return (RuntimeException)t;
		} else {
			return ReflectionUtils.newInstance(runtimeClass, t);
		}
	}
}

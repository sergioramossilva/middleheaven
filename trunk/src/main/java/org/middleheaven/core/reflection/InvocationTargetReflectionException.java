package org.middleheaven.core.reflection;

import java.lang.reflect.InvocationTargetException;

public class InvocationTargetReflectionException extends ReflectionException {

	public InvocationTargetReflectionException(InvocationTargetException t) {
		super(t.getCause());
	}

}

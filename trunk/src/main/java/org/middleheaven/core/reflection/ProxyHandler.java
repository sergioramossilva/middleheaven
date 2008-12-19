package org.middleheaven.core.reflection;

import java.lang.reflect.Method;

public interface ProxyHandler  {

	public Object invoke(Object proxy, Method invokedOnInterface, Method originalOnClass ,Object[] args) throws Throwable;
}

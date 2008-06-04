package org.middleheaven.core.wiring.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.Set;

import org.middleheaven.core.wiring.Resolver;
import org.middleheaven.core.wiring.Scope;

public class ServiceScope implements Scope {

	@Override
	public <T> T scope(Class<T> type, Set<Annotation> annotations,Resolver<T> resolver) {

		return type.cast(Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[]{type}, new ServiceProxy<T>(type)));
	}

	


}

package org.middleheaven.core.dependency;

import org.middleheaven.core.reflection.ReflectionUtils;

public class ClassDependableProperties<T> implements DependableProperties {

	private Class<T> type;
	
	public ClassDependableProperties(Class<T> type) {
		super();
		this.type = type;
	}

	@Override
	public int dependencyCount() {
		return ReflectionUtils.constructors(type).get(0).getParameterTypes().length;
	}

	public Class<T> getType() {
		return type;
	}

}

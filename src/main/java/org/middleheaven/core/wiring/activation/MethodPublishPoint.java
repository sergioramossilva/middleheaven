package org.middleheaven.core.wiring.activation;

import java.lang.reflect.Method;
import java.util.Map;

import org.middleheaven.core.reflection.ReflectionUtils;

public final class MethodPublishPoint implements PublishPoint {

	private Method method;
	private Map<String, String> params;

	public MethodPublishPoint(Method method , Map<String, String> params) {
		this.method = method;
		this.params = params;
	}

	@Override
	public Object getObject(Object target) {
		return ReflectionUtils.invoke(Object.class, method, target);
	}

	@Override
	public Class<?> getPublishedType() {
		return method.getReturnType();
	}

	@Override
	public Map<String, String> getParams() {
		return params;
	}



}

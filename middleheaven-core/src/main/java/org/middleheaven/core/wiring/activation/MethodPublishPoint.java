package org.middleheaven.core.wiring.activation;

import java.lang.reflect.Method;
import java.util.Map;

import org.middleheaven.core.reflection.inspection.MethodIntrospector;

public final class MethodPublishPoint implements PublishPoint {

	private Method method;
	private Map<String, Object> params;

	public MethodPublishPoint(Method method , Map<String,Object> params) {
		this.method = method;
		this.params = params;
	}

	@Override
	public Object getObject(Object target) {
		return MethodIntrospector.of(method).invoke(Object.class, target);
	}

	@Override
	public Class<?> getPublishedType() {
		return method.getReturnType();
	}

	@Override
	public Map<String, Object> getParams() {
		return params;
	}

	public String toString(){
		return method.getDeclaringClass().getName() + "#" + method.getName();
	}

}

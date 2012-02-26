package org.middleheaven.core.wiring.activation;

import java.util.Map;

public interface PublishPoint {

	
	public Class<?> getPublishedType();
	public Object getObject(Object target);
	public Map<String, Object> getParams();

}

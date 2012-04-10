package org.middleheaven.core.wiring;

import java.util.Map;

public interface PublishPoint extends WiringPoint {

	public WiringSpecification[] getSpecifications();
	
	public Class<?> getPublishedType();
	
	public Object getObject(InstanceFactory factory, Object publisherObject);
	
	public Map<String, Object> getParams();

	public String getScope(); 
}

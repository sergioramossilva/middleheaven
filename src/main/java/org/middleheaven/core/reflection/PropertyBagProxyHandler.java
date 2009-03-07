package org.middleheaven.core.reflection;

import java.util.Map;
import java.util.TreeMap;

public class PropertyBagProxyHandler  implements ProxyHandler {
	
	Map<String , Object> properties = new TreeMap<String,Object>();
	
	@Override
	public Object invoke(Object self, Object[] args, MethodDelegator delegator)throws Throwable {
		String name= delegator.getName();
		if (delegator.hasSuper()){
			return delegator.invokeSuper(self, args);  // execute the original method.
		} else if(name.startsWith("get")) {
			return properties.get(name.substring(3));
		} else if(name.startsWith("is")) {
			return properties.get(name.substring(2));
		} else if(name.startsWith("set") && args.length>0) {
			return properties.put(name.substring(3), args[0]);
		}
		return null;
	}
}
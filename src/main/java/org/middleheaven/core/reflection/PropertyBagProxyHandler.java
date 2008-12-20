package org.middleheaven.core.reflection;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

public class PropertyBagProxyHandler  implements ProxyHandler {
	
	Map<String , Object> properties = new TreeMap<String,Object>();
	
	public Object invoke(Object self, Method m, Method original,Object[] args) throws Throwable {
		if (original!=null){
			return original.invoke(self, args);  // execute the original method.
		} else if(m.getName().startsWith("get")) {
			return properties.get(m.getName().substring(3));
		} else if(m.getName().startsWith("is")) {
			return properties.get(m.getName().substring(2));
		} else if(m.getName().startsWith("set") && args.length>0) {
			return properties.put(m.getName().substring(3), args[0]);
		}
		return null;
	}
}
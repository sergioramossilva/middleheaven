package org.middleheaven.core.reflection;

import java.util.Map;
import java.util.TreeMap;

public class PropertyBagProxyHandler  implements ProxyHandler {
	
	Map<String , Object> properties = new TreeMap<String,Object>();
	
	@Override
	public Object invoke(Object self, Object[] args, MethodDelegator delegator)throws Throwable {
		String name= delegator.getName();
		
		if(name.startsWith("get")) {
			return properties.get(name.substring(3).toLowerCase());
		} else if(name.startsWith("is")) {
			return properties.get(name.substring(2).toLowerCase());
		} else if(name.startsWith("set") && args.length>0) {
			return properties.put(name.substring(3).toLowerCase(), args[0]);
		} else if (delegator.hasSuper()){
			try{
				return delegator.invokeSuper(self, args);  // execute the original method.
			} catch (NoSuchMethodError e){
				//no-op
			}
		} 
		
		return null;
	}
	
	public void setProperty(String name, Object value){
		properties.put(name.toLowerCase(), value);
	}
	
	public Object getProperty(String name){
		return properties.get(name.toLowerCase());
	}
}
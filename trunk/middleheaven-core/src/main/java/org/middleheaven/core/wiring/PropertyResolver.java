package org.middleheaven.core.wiring;


/**
 * Allows to retrieve instances form name-keyed , predetermined , values.
 *
 * @param 
 */
public class PropertyResolver implements Resolver {


	private final HashPropertyManager propertyManager = new HashPropertyManager("properties.scope");
	private final WiringService wiringService;

	public PropertyResolver(WiringService wiringService){
		this.wiringService = wiringService;
		
		wiringService.getPropertyManagers().addFirst(propertyManager);
	}
	
	@Override
	public Object resolve(ResolutionContext context, WiringQuery query) {

		Object name = query.getParam("name");

		if (name instanceof String){
			Object obj = wiringService.getPropertyManagers().getProperty((String) name);
			
			if (query.getContract().isAssignableFrom(obj.getClass())){
				return query.getContract().cast(obj);
			} else {
				throw new ClassCastException("Impossible to convert " + obj.getClass().getName() + " to " + query.getContract().getName());
			}
		}

		throw new CannotResolveException(query.getContract(), String.valueOf(name));
	}

	public void setProperty(String name, Object value){
		propertyManager.putProperty(name, value);
	}

}

package org.middleheaven.core.wiring;


public class ProviderResolver implements Resolver {

	Class<Provider<?>>  providerClass;
	WiringService wiringService;
	
	ProviderResolver(WiringService wiringService , Class<Provider<?>> providerClass){
		this.providerClass = providerClass;
		this.wiringService = wiringService;
	}
	
	@Override
	public Object resolve(ResolutionContext context, WiringQuery query) {
		return wiringService.getInstance(providerClass).provide();
	}

}

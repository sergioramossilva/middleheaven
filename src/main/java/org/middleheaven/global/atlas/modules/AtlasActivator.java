package org.middleheaven.global.atlas.modules;

import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.global.atlas.AtlasService;

public class AtlasActivator extends Activator {

	
	private final ModularAtlasService service;
	
	public AtlasActivator(){
		service = new ModularAtlasService();
	}
	
	@Publish 
	public AtlasService getService(){
		return service;
	}
	

	@Override
	public void activate(ActivationContext context) {}

	@Override
	public void inactivate(ActivationContext context) {}

	
}

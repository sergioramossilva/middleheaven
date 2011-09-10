package org.middleheaven.global.atlas;

import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.global.atlas.modules.ModularAtlasService;

public class AtlasActivator extends Activator {

	
	private final AtlasService service = new ModularAtlasService();
	
	public AtlasActivator(){
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

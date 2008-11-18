package org.middleheaven.global.atlas.modules;

import org.middleheaven.core.services.Publish;
import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.global.atlas.AtlasService;

public class AtlasActivator extends ServiceActivator {

	
	private final ModularAtlasService service;
	
	public AtlasActivator(){
		service = new ModularAtlasService();
	}
	
	@Publish 
	public AtlasService getService(){
		return service;
	}
	
	@Override
	public void activate(ServiceAtivatorContext context) {}

	@Override
	public void inactivate(ServiceAtivatorContext context) {}

}

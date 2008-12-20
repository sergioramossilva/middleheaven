package org.middleheaven.application;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.Container;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.wiring.WiringContext;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.util.LiveCollection;
import org.middleheaven.util.Version;

public class TransientApplicationContext implements ApplicationContext {

	Map<String, ApplicationModule> modules = new HashMap<String, ApplicationModule>();
	Collection<ApplicationModule> all = new LiveCollection<ApplicationModule>();
	
	private Container container;
	private WiringContext wcontext;
	
	public TransientApplicationContext(WiringContext wcontext, Container configContext) {
		this.container = configContext;
		this.wcontext = wcontext;
	}

	public ApplicationModule getOlderModulePresent(ModuleID moduleID) {
		ApplicationModule currentModule = modules.get(moduleID.getIdentifier());
		
		if (currentModule!=null && currentModule.getModuleID().getVersion().compareTo(moduleID.getVersion())<=0){
			return currentModule;
		} else { 
			return null;
		}
	}
	
	public Collection<ApplicationModule> modules(){
		return all;
	}
	
	@Override
	public void addModule(ApplicationModule module) {
		
		wcontext.wireMembers(module);
		
		ApplicationModule currentModule = modules.get(module.getModuleID().getIdentifier());
		if (currentModule==null){
			modules.put(module.getModuleID().getIdentifier(),module);
			all.add(module);
		} else {
			Version candidate = module.getModuleID().getVersion();
			Version current = currentModule.getModuleID().getVersion();
			
			// if it is a newer version
			if (candidate.compareTo(current)>0){
				modules.put(module.getModuleID().getIdentifier(),module);
				all.add(module);
			}
		}
	}
	
	@Override
	public boolean isCompatibleModulePresent(String identifier, Version version) {
		ApplicationModule currentModule = modules.get(identifier);
		
		return currentModule!=null && currentModule.getModuleID().getVersion().compareTo(version)>=0;
	}

	public ManagedFile getAppClasspathRepository() {
		return container.getAppClasspathRepository();
	}


	public ManagedFile getAppConfigRepository() {
		return container.getAppConfigRepository();
	}

	public ManagedFile getAppDataRepository() {
		return container.getAppDataRepository();
	}


	public ManagedFile getAppLogRepository() {
		return container.getAppLogRepository();
	}

}

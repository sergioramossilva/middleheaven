package org.middleheaven.application;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.util.Version;

public class TransientApplicationContext implements ApplicationContext {

	Map<String, ApplicationModule> modules = new HashMap<String, ApplicationModule>();
	
	
	public ApplicationModule getOlderModulePresent(ModuleID moduleID) {
		ApplicationModule currentModule = modules.get(moduleID.getIdentifier());
		
		if (currentModule!=null && currentModule.getModuleID().getVersion().compareTo(moduleID.getVersion())<=0){
			return currentModule;
		} else { 
			return null;
		}
	}
	
	public Collection<ApplicationModule> modules(){
		return Collections.unmodifiableCollection(modules.values());
		
	}
	@Override
	public void addModule(ApplicationModule module) {
		ApplicationModule currentModule = modules.get(module.getModuleID().getIdentifier());
		if (currentModule==null){
			modules.put(module.getModuleID().getIdentifier(),module);
		} else {
			Version candidate = module.getModuleID().getVersion();
			Version current = currentModule.getModuleID().getVersion();
			
			// if it is a newer version
			if (candidate.compareTo(current)>0){
				modules.put(module.getModuleID().getIdentifier(),module);
			}
		}
	}
	
	@Override
	public boolean isCompatibleModulePresent(String identifier, Version version) {
		ApplicationModule currentModule = modules.get(identifier);
		
		return currentModule!=null && currentModule.getModuleID().getVersion().compareTo(version)>=0;
	}

}

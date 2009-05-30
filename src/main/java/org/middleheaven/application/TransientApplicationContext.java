package org.middleheaven.application;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.BootstrapContainer;
import org.middleheaven.core.wiring.ObjectPool;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.util.Version;
import org.middleheaven.util.collections.LiveCollection;

public class TransientApplicationContext implements ApplicationContext {

	Map<String, ApplicationModule> modules = new HashMap<String, ApplicationModule>();
	Collection<ApplicationModule> all = new LiveCollection<ApplicationModule>();
	
	private BootstrapContainer container;
	private ObjectPool wcontext;
	
	public TransientApplicationContext(ObjectPool wcontext, BootstrapContainer configContext) {
		this.container = configContext;
		this.wcontext = wcontext;
	}
	
	public Collection<ApplicationModule> modules(){
		return all;
	}
	
	@Override
	public void addModule(ApplicationModule module) {
		
		// inject after creation
		wcontext.wireMembers(module);
		
		ApplicationModule currentModule = modules.get(module.getModuleID().getIdentifier());
		if (currentModule==null){
			// new module
			modules.put(module.getModuleID().getIdentifier(),module);
			all.add(module);
		} else {
			
			Version candidate = module.getModuleID().getVersion();
			Version current = currentModule.getModuleID().getVersion();
			
			// if it is a newer version
			if (candidate.compareTo(current)>0){
				// unload older
				currentModule.unload(this);
				// install newer
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

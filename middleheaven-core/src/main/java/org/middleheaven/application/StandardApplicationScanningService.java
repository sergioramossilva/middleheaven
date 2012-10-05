/**
 * 
 */
package org.middleheaven.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.logging.Logger;
import org.middleheaven.util.Version;

/**
 *  Standard implementation of {@link ApplicationScanningService}.
 */
public class StandardApplicationScanningService implements
		ApplicationScanningService {

	
	private Set<ModuleScanner> moduleScnanners =  new CopyOnWriteArraySet<ModuleScanner>();
	private Set<ApplicationScanner> appScnanners =  new CopyOnWriteArraySet<ApplicationScanner>();
	private ApplicationService applicationService;
	 
	
	public StandardApplicationScanningService ( ApplicationService applicationService){
		this.applicationService = applicationService;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addApplicationScanner(ApplicationScanner scanner) {
		appScnanners.add(scanner);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addModuleScanner(ModuleScanner scanner) {
		moduleScnanners.add(scanner);
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scan() {
		
		Logger.onBookFor(ApplicationScanningService.class).info("Scanning for applications and modules.");
		
		
		Collection<Application> apps = new ArrayList<Application>(1);
		
		for (ApplicationScanner scanner : this.appScnanners){
			scanner.scan(apps);
		}
		
		
		for (Application app : apps ){
			applicationService.registry(app.getApplicationId(), app.getVersion(), app.getActivator());
		}
		
		Collection<Module> modules = new ArrayList<Module>(3);
		
		for (ModuleScanner scanner : moduleScnanners){
			scanner.scan(modules);
		}
		

		for (Module module : modules){
			
			ApplicationRegistry registry = applicationService.getRegistry(module.getApplicationId());
			
			if (registry == null){
				registry = applicationService.registry(module.getApplicationId(), Version.unknown(), new NullApplicationActivator());
			}
			
			registry.addModule(module);
		}
		
		Logger.onBookFor(ApplicationScanningService.class).info("Scanning done.");
		
	}


}

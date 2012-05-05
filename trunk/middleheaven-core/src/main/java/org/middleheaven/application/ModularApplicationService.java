/**
 * 
 */
package org.middleheaven.application;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.util.Version;

class ModularApplicationService implements ApplicationService {

	private final AbstractDynamicLoadApplicationServiceActivator abstractDynamicLoadApplicationServiceActivator;

	ApplicationContext appContext = new ApplicationContext(){

		@Override
		public ApplicationVersion getApplication() {
			return ModularApplicationService.this.abstractDynamicLoadApplicationServiceActivator.application;
		}

		@Override
		public Collection<ModuleActivator> getModules() {
			return Collections.unmodifiableCollection(modules);
		}

		@Override
		public boolean isModulePresent(String name) {
			return modulesNamesMapping.containsKey(name);
		}

		@Override
		public boolean isCompatibleModulePresent(ModuleVersion version) {

			ModuleVersion currentModule = modulesNamesMapping.get(version.getName());

			return currentModule!=null && currentModule.getVersion().compareTo(version.getVersion())>=0;

		}

		@Override
		public ServiceContext getServiceContext() {
			return ModularApplicationService.this.abstractDynamicLoadApplicationServiceActivator.serviceContext;
		}

	};

	private ApplicationCycleState state = ApplicationCycleState.STOPED;

	private Deque<ModuleVersion> modulesVersions = new LinkedList<ModuleVersion>();
	private Deque<ModuleActivator> modules = new LinkedList<ModuleActivator>();
	private Map<String, ModuleVersion> modulesNamesMapping = new HashMap<String, ModuleVersion>();

	private Set<ModuleListener> moduleListeners =  new CopyOnWriteArraySet<ModuleListener>();
	private Set<ApplicationListener> listeners = new CopyOnWriteArraySet<ApplicationListener>();



	public ModularApplicationService(AbstractDynamicLoadApplicationServiceActivator abstractDynamicLoadApplicationServiceActivator) {
		this.abstractDynamicLoadApplicationServiceActivator = abstractDynamicLoadApplicationServiceActivator;
	}


	public void addModule(ModuleVersion module) {

		ModuleVersion currentModule = modulesNamesMapping.get(module.getName());

		if (currentModule==null){
			// new module
			modulesVersions.add(module);
			modulesNamesMapping.put(module.getName(), module);

		} else {

			Version candidate = module.getVersion();
			Version current = currentModule.getVersion();

			// if it is a newer version
			if (candidate.compareTo(current)>0){
				// unload older
				// TODO currentModule.unload(this);

				// install newer
				modulesNamesMapping.put(module.getName(),module);
				modulesVersions.add(module);
			}
		}
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addApplicationListener(ApplicationListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeApplicationListener(ApplicationListener listener) {
		this.listeners.remove(listener);
	}

	protected boolean setState(ApplicationCycleState phase){
		if ( state.canChangeTo(phase)){
			state = phase;
			fireEvent(new ApplicationEvent(phase));
			return true;
		}
		return false;
	}

	private void fireEvent (ApplicationEvent event){
		for (ApplicationListener l: listeners){
			l.onCycleStateChanged(event);
		}
	}

	protected void start() {

		if (this.setState(ApplicationCycleState.STOPED)){
			this.abstractDynamicLoadApplicationServiceActivator.log.info("Scanning for applications");
			
			this.abstractDynamicLoadApplicationServiceActivator.loadPresentModules();
			
			this.abstractDynamicLoadApplicationServiceActivator.log.info("Activating applications");

			for (Iterator<ModuleVersion> it = this.getModules().descendingIterator(); it.hasNext(); ){
				ModuleVersion module = it.next();

				for (ModuleListener moduleListener :  moduleListeners){

					try {
						if (moduleListener.isListenerOf(module)){
							moduleListener.onStart(module, appContext);
						}

					} catch (RuntimeException e){
						this.abstractDynamicLoadApplicationServiceActivator.log.error(e  , "Impossible to activate listener {0} for module {1}" , moduleListener.getClass(), module);
						throw e;
					}
				}

			}



			this.setState(ApplicationCycleState.LOADED);
			this.setState(ApplicationCycleState.READY);

			this.abstractDynamicLoadApplicationServiceActivator.log.info("Applications ready");
		}

	}


	/**
	 * @return
	 */
	private Deque<ModuleVersion> getModules() {
		return modulesVersions;
	}

	protected void stop() {
		this.setState(ApplicationCycleState.PAUSED);
		this.abstractDynamicLoadApplicationServiceActivator.log.info("Deactivating applications");


		for (Iterator<ModuleVersion> it = this.getModules().iterator(); it.hasNext(); ){
			ModuleVersion module = it.next();

			for (ModuleListener moduleListener : moduleListeners){
				try {
					moduleListener.onStop(module, appContext);
				} catch (Exception e){
					this.abstractDynamicLoadApplicationServiceActivator.log.warn(e,"Impossible to deactivate listener {0} for module {1}", moduleListener.getClass(),  module);
				}
			}
		}

		this.setState(ApplicationCycleState.STOPED);
		this.abstractDynamicLoadApplicationServiceActivator.log.info("Applications deactivated");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addModuleListener(ModuleListener listener) {
		this.moduleListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeModuleListener(ModuleListener listener) {
		throw new UnsupportedOperationException("Not implememented yet");
	}




}
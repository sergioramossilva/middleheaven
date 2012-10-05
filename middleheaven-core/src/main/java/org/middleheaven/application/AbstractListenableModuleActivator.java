/**
 * 
 */
package org.middleheaven.application;

import javax.swing.event.EventListenerList;

import org.middleheaven.events.EventListenersSet;

/**
 * 
 */
public abstract class AbstractListenableModuleActivator implements ListenableModuleActivator {


	
	private EventListenersSet<ModuleActivatorListener> eventListeners =  EventListenersSet.newSet(ModuleActivatorListener.class);
			
	public AbstractListenableModuleActivator (){}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void start(ApplicationContext context) {
		doStartListenableModule(context);
		
		eventListeners.broadcastEvent().onModuleStart(new ModuleActivationEvent(this));
		
	}
	
	protected abstract void doStartListenableModule(ApplicationContext context);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop(ApplicationContext context) {
		doStopListenableModule(context);
		
		eventListeners.broadcastEvent().onModuleStop(new ModuleActivationEvent(this));
	}

	protected abstract void doStopListenableModule(ApplicationContext context);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addModuleActivatorListener(ModuleActivatorListener listener) {
		this.eventListeners.addListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeModuleActivatorListener(ModuleActivatorListener listener) {
		this.eventListeners.removeListener(listener);
	}

}

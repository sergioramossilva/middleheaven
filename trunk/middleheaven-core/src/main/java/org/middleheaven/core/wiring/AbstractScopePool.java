/**
 * 
 */
package org.middleheaven.core.wiring;

import org.middleheaven.events.EventListenersSet;

/**
 * 
 */
public abstract class AbstractScopePool implements ScopePool {

	
	private final EventListenersSet<ScopeListener> event = EventListenersSet.newSet(ScopeListener.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addScopeListener(ScopeListener listener) {
		event.addListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeScopeListener(ScopeListener listener) {
		event.removeListener(listener);
	}
	
	protected final void fireObjectAdded(Object object){
		event.broadcastEvent().onObjectAdded(new ScopePoolEvent(object, this));
	}

	protected final void fireObjectRemoved(Object object){
		event.broadcastEvent().onObjectRemoved(new ScopePoolEvent(object, this));
	}
}

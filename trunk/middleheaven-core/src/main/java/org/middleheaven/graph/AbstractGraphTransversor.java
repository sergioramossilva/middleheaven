package org.middleheaven.graph;

import org.middleheaven.events.EventListenersSet;


/**
 * 
 */
public abstract class AbstractGraphTransversor implements GraphTransversor {

	private final EventListenersSet<GraphTranverseListener> listenerSet = EventListenersSet.newSet(GraphTranverseListener.class);
	
	@Override
	public void addListener(GraphTranverseListener listener) {
		listenerSet.addListener(listener);
	}

	@Override
	public void removeListener(GraphTranverseListener listener) {
		listenerSet.removeListener(listener);
	}

	protected final EventListenersSet<GraphTranverseListener> getListenerSet(){
		return listenerSet;
	}
	
	
}

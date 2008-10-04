package org.middleheaven.util.sequence;

import org.middleheaven.events.EventListenersSet;

public abstract class AbstractStatePersistanteSequence<T> implements StatePersistentSequence<T> {


    private EventListenersSet<SequenceStateListener> listeners = EventListenersSet.newSet(SequenceStateListener.class);
    private String name;
    
	protected AbstractStatePersistanteSequence(String name) {
		this.name= name;
	}

    protected void fireStateChange(SequenceState state){
    	listeners.broadcastEvent().onStateChanged(new StateChangedEvent(state));
    }
    
	@Override
	public void addSequenceStateListener(SequenceStateListener listener) {
		listeners.addListener(listener);
	}

	@Override
	public String getName(){
		return name;
	}


}

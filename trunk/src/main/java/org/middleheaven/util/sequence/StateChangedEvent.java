package org.middleheaven.util.sequence;

public class StateChangedEvent {

	private SequenceState state;
	private StatePersistentSequence<?> sequence;
	
	public StateChangedEvent(SequenceState state) {
		super();
		this.state = state;
	}
	
	public SequenceState getSequenceState(){
		return state;
	}
}

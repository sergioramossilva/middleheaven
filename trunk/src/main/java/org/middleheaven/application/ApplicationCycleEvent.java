package org.middleheaven.application;

public class ApplicationCycleEvent {

	ApplicationCycleState phase;

	public ApplicationCycleEvent(ApplicationCycleState phase) {
		super();
		this.phase = phase;
	}
	
}

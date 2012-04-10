package org.middleheaven.application;

public class ApplicationEvent {

	ApplicationCycleState phase;

	public ApplicationEvent(ApplicationCycleState phase) {
		super();
		this.phase = phase;
	}
	
}

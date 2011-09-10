package org.middleheaven.application;

public enum ApplicationCycleState {

	STOPED,
	LOADED,
	PAUSED,
	READY;

	public boolean canChangeTo(ApplicationCycleState phase) {
		switch (this){
		case STOPED:
			return phase == STOPED || phase == LOADED;
		case LOADED:
			return phase == READY;
		case READY:
			return phase == PAUSED;
		case PAUSED:
			return phase == READY || phase == STOPED;
		default :
			return false;
		
		}
	}

}

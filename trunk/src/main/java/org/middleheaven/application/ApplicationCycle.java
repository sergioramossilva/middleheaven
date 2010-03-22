package org.middleheaven.application;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

abstract class  DefaultApplicationLoadingCycle implements ApplicationLoadingCycle {

	private Set<ApplicationCycleListener> listeners = new CopyOnWriteArraySet<ApplicationCycleListener>(); 
	private ApplicationCycleState state = ApplicationCycleState.STOPED;
	
	@Override
	public void addApplicationCycleListener(ApplicationCycleListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeApplicationCycleListener(ApplicationCycleListener listener) {
		this.listeners.remove(listener);
	}
	
	public final boolean setState(ApplicationCycleState phase){
		if ( state.canChangeTo(phase)){
			state = phase;
			fireEvent(new ApplicationCycleEvent(phase));
			return true;
		}
		return false;
	}
	
	private void fireEvent (ApplicationCycleEvent event){
		for (ApplicationCycleListener l: listeners){
			l.onCycleStateChanged(event);
		}
	}



}

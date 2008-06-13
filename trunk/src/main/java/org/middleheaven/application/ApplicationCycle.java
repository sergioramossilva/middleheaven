package org.middleheaven.application;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.io.repository.FileChangeListener;

abstract class  DefaultApplicationLoadingCycle implements ApplicationLoadingCycle, FileChangeListener {

	private Set<ApplicationCycleListener> listeners = new CopyOnWriteArraySet<ApplicationCycleListener>(); 
	
	@Override
	public void addApplicationCycleListener(ApplicationCycleListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeApplicationCycleListener(ApplicationCycleListener listener) {
		this.listeners.remove(listener);
	}
	
	public void setState(ApplicationCycleState phase){
		fireEvent(new ApplicationCycleEvent(phase));
	}
	
	private void fireEvent (ApplicationCycleEvent event){
		for (ApplicationCycleListener l: listeners){
			l.onCycleStateChanged(event);
		}
	}



}

package org.middleheaven.application;

public interface ApplicationLoadingCycle {

	public void start();
	public void stop();
	
	public void addApplicationCycleListener(ApplicationCycleListener listener);
	public void removeApplicationCycleListener(ApplicationCycleListener listener);
}

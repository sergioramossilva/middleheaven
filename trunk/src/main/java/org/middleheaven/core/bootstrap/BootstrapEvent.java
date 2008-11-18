package org.middleheaven.core.bootstrap;

public class BootstrapEvent {

	
	boolean isBootup;
	boolean isStart;
	
	public BootstrapEvent(boolean isBootup, boolean isStart){
		this.isBootup = isBootup;
		this.isStart = isStart;
	}

	public boolean isBootup() {
		return isBootup;
	}

	public boolean isBootdown() {
		return !isBootup;
	}
	
	public boolean isStart() {
		return isStart;
	}
	
	public boolean isEnd() {
		return !isStart;
	}
}

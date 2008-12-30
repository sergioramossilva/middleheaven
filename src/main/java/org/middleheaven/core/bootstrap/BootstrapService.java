package org.middleheaven.core.bootstrap;


public interface BootstrapService {

	
	public ExecutionEnvironmentBootstrap getEnvironmentBootstrap();
	
	public void addListener(BootstapListener listener);
	public void removeListener(BootstapListener listener);
}

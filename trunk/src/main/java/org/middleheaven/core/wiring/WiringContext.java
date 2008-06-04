package org.middleheaven.core.wiring;


public interface WiringContext {

	
	public <T> T getInstance(Class<T> type);

	public WiringContext addConfiguration(BindConfiguration ... configuration); 

}

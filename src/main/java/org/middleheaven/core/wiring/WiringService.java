package org.middleheaven.core.wiring;

/**
 * Wire Service ?
 * 
 */
public interface WiringService {

	
	public WiringContext getInjector();
	public void addConnector(InjectionConnector ... connectors); 
}

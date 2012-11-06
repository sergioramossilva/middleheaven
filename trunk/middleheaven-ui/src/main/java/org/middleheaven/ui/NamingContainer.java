package org.middleheaven.ui;


/**
 * Identifies containers that have support for id retrival.
 */
public interface NamingContainer {

	public UIComponent findContainedComponent(String componentID); 
	
}

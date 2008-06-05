package org.middleheaven.ui;

import java.util.Set;


public interface NamingContainer {

	public Set<UIComponent> findContainedComponent(String componentID); 
	
}

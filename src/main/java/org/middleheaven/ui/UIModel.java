package org.middleheaven.ui;

import java.beans.PropertyChangeListener;

public interface UIModel {

	
	public void addPropertyChangeListener(PropertyChangeListener listener);
	public void removePropertyChangeListener(PropertyChangeListener listener);
	
	
}

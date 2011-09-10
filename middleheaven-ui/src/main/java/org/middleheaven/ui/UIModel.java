package org.middleheaven.ui;

import java.beans.PropertyChangeListener;

import org.middleheaven.ui.events.UIFocusEvent;

public interface UIModel {


	public void setEnabled(boolean enabled);
	public boolean isEnabled();
	
	public void addPropertyChangeListener(PropertyChangeListener listener);
	public void removePropertyChangeListener(PropertyChangeListener listener);
	
	public void onFocusGained(UIFocusEvent event);
	public void onFocusLost(UIFocusEvent event);
	
}

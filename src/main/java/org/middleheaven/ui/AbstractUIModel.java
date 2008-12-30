package org.middleheaven.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.middleheaven.ui.events.UIFocusEvent;

public abstract class AbstractUIModel implements UIModel {

	private List<PropertyChangeListener> propertyChangeListeners = new CopyOnWriteArrayList<PropertyChangeListener>();
	
	boolean enabled;
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = firePropertyChange("enabled", this.enabled , enabled);
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeListeners.add(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeListeners.remove(listener);
	}

	protected <T> T firePropertyChange (String propertyName, T oldValue, T newValue){
		PropertyChangeEvent event = new PropertyChangeEvent(this,propertyName,oldValue,newValue);
		
		for (PropertyChangeListener listener : this.propertyChangeListeners){
			listener.propertyChange(event);
		}
		
		return newValue;
	}
	
	@Override
	public void onFocusGained(UIFocusEvent event) {
		// no-op
		
	}

	@Override
	public void onFocusLost(UIFocusEvent event) {
		// no-op
	}


}

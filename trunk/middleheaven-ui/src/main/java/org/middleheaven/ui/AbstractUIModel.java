package org.middleheaven.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractUIModel implements UIModel {

	private List<PropertyChangeListener> propertyChangeListeners = new CopyOnWriteArrayList<PropertyChangeListener>();
	
	boolean enabled;
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = firePropertyChange("enabled", this.enabled , enabled);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeListeners.add(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeListeners.remove(listener);
	}

	protected <T> T firePropertyChange (String propertyName, T oldValue, T newValue){
		if (newValue!=null && newValue.equals(oldValue)) {
			return newValue;
		}
		
		PropertyChangeEvent event = new PropertyChangeEvent(this,propertyName,oldValue,newValue);
		
		for (PropertyChangeListener listener : this.propertyChangeListeners){
			listener.propertyChange(event);
		}
		
		return newValue;
	}
	

}

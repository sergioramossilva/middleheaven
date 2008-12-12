package org.middleheaven.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.models.UIContainerModel;


public class AbstractUIContainerModel implements UIContainerModel {

	
	private List<PropertyChangeListener> propertyChangeListeners = new CopyOnWriteArrayList<PropertyChangeListener>();
	
	/**
	 * Default implementation 
	 * @return all children in the component
	 */
	@Override 
	public List<UIComponent> getChildrenComponents(UIComponent component) {
		return component.getChildrenComponents();
	}

	@Override
	public UILayout getLayout(UIContainer component) {
		return component.getLayout();
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeListeners.add(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeListeners.remove(listener);
	}

	protected <T> void firePropertyChange (String propertyName, T oldValue, T newValue){
		PropertyChangeEvent event = new PropertyChangeEvent(this,propertyName,oldValue,newValue);
		
		for (PropertyChangeListener listener : this.propertyChangeListeners){
			listener.propertyChange(event);
		}
	}
}

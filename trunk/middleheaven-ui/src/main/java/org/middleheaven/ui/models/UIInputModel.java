package org.middleheaven.ui.models;

import java.beans.PropertyChangeListener;

public interface UIInputModel extends UIOutputModel{

	
	public void setName(String name);
	
	public String getName();

	// TODO use Readonlystate vs enable
	
	/**
	 * Changes the the enable state.
	 *  
	 * @param enabled <code>true</code> if this input is available for input, <code>false</code> otherwise.
	 */
	public void setEnabled(boolean enabled);
	
	/**
	 * If this component is available for input.
	 * @return <code>true</code> if this input is available for input, <code>false</code> otherwise.
	 * 
	 */
	public boolean isEnabled();
	
	public void addPropertyChangeListener(PropertyChangeListener listener);
	public void removePropertyChangeListener(PropertyChangeListener listener);
	
	
}

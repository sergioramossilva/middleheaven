package org.middleheaven.ui.models;

import java.util.Collection;

import org.middleheaven.ui.UIActionHandler;

/**
 * The command model.
 * 
 * Comands are designated by a name.
 */
public interface UICommandModel extends UITextLabeledModel{

	/**
	 * The name of the command trigged by this model.
	 * @return  The name of the command trigged by this model.
	 */
	public String getName();
	
	/**
	 * The name of the command trigged by this model.
	 * @param name The name of the command trigged by this model.
	 */
	public void setName(String name);
	
	/**
	 * Changes the the enable state.
	 *  
	 * @param enabled <code>true</code> if this command is available for execution, <code>false</code> otherwise.
	 */
	public void setEnabled(boolean enabled);
	
	/**
	 * If this command is available for execution.
	 * @return <code>true</code> if this command is available for execution, <code>false</code> otherwise.
	 * 
	 */
	public boolean isEnabled();
	
	
	public Collection<UIActionHandler> getHandlers();
}

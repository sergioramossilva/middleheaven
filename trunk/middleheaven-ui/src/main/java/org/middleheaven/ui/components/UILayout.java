package org.middleheaven.ui.components;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.models.UILayoutModel;

/**
 * Manages the localization of ist children in a given display area.
 * 
 */
public interface UILayout extends UIComponent{

	 public void addComponent (UIComponent component, UILayoutConstraint layoutConstrain);
	 public void removeComponent (UIComponent component);
	    
	/**
	 * 
	 * {@inheritDoc}
	 */
	public UILayoutModel getUIModel();
}

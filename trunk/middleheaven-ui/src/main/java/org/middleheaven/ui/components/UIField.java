package org.middleheaven.ui.components;

import org.middleheaven.ui.models.UIFieldInputModel;

/**
 * Represent a single value user inputed value (normally by diitation)
 */
public interface UIField extends UIInput{

	
	public UIFieldInputModel getUIModel();
	
}

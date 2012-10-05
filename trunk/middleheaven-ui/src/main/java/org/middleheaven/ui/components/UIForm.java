package org.middleheaven.ui.components;

import org.middleheaven.ui.models.UIFormModel;


public interface UIForm extends UIContainer{

	
	public UICommandSet getCommandSet();
	
	public UIFormModel getUIModel();
}

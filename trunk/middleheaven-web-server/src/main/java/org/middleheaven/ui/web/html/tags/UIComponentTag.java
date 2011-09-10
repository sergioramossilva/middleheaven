package org.middleheaven.ui.web.html.tags;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIModel;

public interface UIComponentTag {

	
	public void setEnabled(boolean enabled);
	public UIModel getModel();
	public Class<? extends UIComponent> getComponentType();
	
	public UIComponent getUIComponent();
}

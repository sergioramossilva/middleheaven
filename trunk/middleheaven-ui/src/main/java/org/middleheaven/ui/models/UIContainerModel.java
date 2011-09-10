package org.middleheaven.ui.models;

import java.util.List;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.components.UILayout;


public interface UIContainerModel extends UIModel {

	
	public List<UIComponent> getChildrenComponents(UIComponent component);
	public UILayout getLayout(UIContainer component);
}

package org.middleheaven.ui;

import java.util.List;


public interface UIContainerModel extends UIModel {

	
	public List<UIComponent> getChildrenComponents(UIComponent component);
	public UILayout getLayout(UIContainer component);
}

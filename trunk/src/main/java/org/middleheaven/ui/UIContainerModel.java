package org.middleheaven.ui;

import java.util.Set;


public interface UIContainerModel extends UIModel {

	
	public Set<UIComponent> getChildrenComponents(UIComponent component);
	public UILayout getLayout(UIContainer component);
}

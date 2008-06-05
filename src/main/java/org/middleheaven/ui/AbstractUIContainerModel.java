package org.middleheaven.ui;

import java.util.Set;


public class AbstractUIContainerModel implements UIContainerModel {

	/**
	 * Default implementation 
	 * @return all children in the component
	 */
	@Override 
	public Set<UIComponent> getChildrenComponents(UIComponent component) {
		return component.getChildrenComponents();
	}

	@Override
	public UILayout getLayout(UIContainer component) {
		return component.getLayout();
	}

}

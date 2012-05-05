package org.middleheaven.ui;

import java.util.List;

import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.models.UIContainerModel;


public class AbstractUIContainerModel extends AbstractUIModel implements UIContainerModel {

	/**
	 * Default implementation 
	 * @return all children in the component
	 */
	@Override 
	public List<UIComponent> getChildrenComponents(UIComponent component) {
		return component.getChildrenComponents();
	}

	@Override
	public UILayout getLayout(UIContainer component) {
		return component.getUIContainerLayout();
	}






}

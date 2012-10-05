package org.middleheaven.ui.models;

import java.util.List;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.components.UILayout;

/**
 * Auxliar {@link UIModel} interface to enhance models that can act as containers.
 * 
 */
public interface UIContainerModel extends UIModel {

	/**
	 * Obtains the children components. This can be implements so the components are retrives dinamicly.
	 * 
	 * @param component the parent component.
	 * 
	 * @return the components children.
	 */
	public List<UIComponent> getChildrenComponents(UIComponent component);
	
	/**
	 * The layout used by a given container.
	 * 
	 * @param component the container.
	 * @return the container's layout.
	 */ 
	public UILayout getLayout(UIContainer component);
}

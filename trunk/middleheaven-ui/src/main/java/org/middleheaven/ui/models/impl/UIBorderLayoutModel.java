package org.middleheaven.ui.models.impl;

import org.middleheaven.ui.ComponentAggregationEvent;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.models.UILayoutModel;
import org.middleheaven.util.collections.BoundMap;
import org.middleheaven.util.collections.HashBoundMap;

/**
 * Border Layout.
 */
public class UIBorderLayoutModel implements UILayoutModel {

	public enum UIBorderLayoutConstraint implements UILayoutConstraint{
		NORTH,
		SOUTH,
		WEST,
		EAST,
		CENTER
	}

	// map the component id to its position
	private final BoundMap<String, UIBorderLayoutConstraint> children = new HashBoundMap<String, UIBorderLayoutConstraint>();

	
	@Override
	public void componentAdded(ComponentAggregationEvent event) {
		if (UIBorderLayoutConstraint.class.isInstance(event.getConstraint())){
			children.put(event.getComponent().getGID(), (UIBorderLayoutConstraint)event.getConstraint());
		}
	}

	@Override
	public void componentRemoved(ComponentAggregationEvent event) {
		
		children.remove(event.getComponent().getGID());

	}

	@Override
	public UISize getLayoutablePreferedSize(UISize layoutable) {
		return layoutable;
	}

	public UIBorderLayoutConstraint getBorderConstraintFor(UIComponent component){
		return this.children.get(component.getGID());
	}
	
	/**
	 * 
	 * @param contraint
	 * @return the id of the component positioned at the given constraint, or <code>null</code> if no component is at that position
	 */
	public String getBorderComponentId(UIBorderLayoutConstraint contraint){
		return this.children.reversed().get(contraint);
	}
	
}

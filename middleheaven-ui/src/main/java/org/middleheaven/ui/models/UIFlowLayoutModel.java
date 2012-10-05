package org.middleheaven.ui.models;

import org.middleheaven.ui.ComponentAggregationEvent;
import org.middleheaven.ui.UISize;

/**
 * Flow Layout
 */
public class UIFlowLayoutModel implements UILayoutModel {

	
	public UIFlowLayoutModel (){}
	
	@Override
	public void componentAdded(ComponentAggregationEvent event) {}

	@Override
	public void componentRemoved(ComponentAggregationEvent event) {}

	@Override
	public UISize getLayoutablePreferedSize(UISize layoutable) {
		return layoutable;
	}


}

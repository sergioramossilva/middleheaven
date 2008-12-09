package org.middleheaven.ui.layout;

import java.util.List;

import org.middleheaven.ui.ComponentAggregationEvent;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.models.UILayoutModel;

public class UIFlowLayoutModel implements UILayoutModel {

	@Override
	public void componentAdded(ComponentAggregationEvent event) {}

	@Override
	public void componentRemoved(ComponentAggregationEvent event) {}

	@Override
	public UIDimension getLayoutablePreferedSize(UIDimension layoutable) {
		return layoutable;
	}

	@Override
	public void invalidateCache(Object container) {
		// no-op
	}

	@Override
	public void applyLayout(UIComponent container) {

		List<UIComponent> children = container.getChildrenComponents();

		int remainingWidth = container.getWidth();

		int previousX =0; 
		for (UIComponent component : children){
			int componentWidth = component.getWidth();

			if (remainingWidth < componentWidth){
				// no more space available
				component.setVisible(false);
				remainingWidth = -1; // force conditions for reamining componets
			} else {
				remainingWidth -=  componentWidth;
				previousX += componentWidth;
				component.setPosition(previousX, 0);
			}

		}
	}

}

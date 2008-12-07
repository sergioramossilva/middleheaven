package org.middleheaven.ui.layout;

import java.util.List;

import org.middleheaven.ui.ComponentAggregationEvent;
import org.middleheaven.ui.UIArea;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutModel;

public class UIFlowLayoutModel implements UILayoutModel {

	@Override
	public void componentAdded(ComponentAggregationEvent event) {}

	@Override
	public void componentRemoved(ComponentAggregationEvent event) {}

	@Override
	public UIArea getLayoutablePreferedSize(UIArea layoutable) {
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
				component.setBounds(previousX, 0);
			}

		}
	}

}

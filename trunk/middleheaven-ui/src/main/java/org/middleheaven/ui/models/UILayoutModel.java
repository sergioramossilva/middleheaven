package org.middleheaven.ui.models;

import org.middleheaven.ui.ComponentAggregationEvent;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.components.UILayout;


/**
 * A specific {@link UIModel} for the {@link UILayout} component.
 */
public interface UILayoutModel extends UIModel {

	/**
	 * Let the layout model calculate the correct UIArea
	 * @param layoutable
	 * @return
	 */
    public UISize getLayoutablePreferedSize(UISize layoutable);
    
  
    /**
     * Trigged when a new component is add to the container
     * @param event
     */
    public void componentAdded(ComponentAggregationEvent event);

    /**
     * Trigged when a new component is removed from the container
     * @param event
     */
    public void componentRemoved(ComponentAggregationEvent event);
    
}

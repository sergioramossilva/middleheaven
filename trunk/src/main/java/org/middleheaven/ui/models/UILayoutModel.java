package org.middleheaven.ui.models;

import org.middleheaven.ui.ComponentAggregationEvent;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIDimension;



public interface UILayoutModel {

	/**
	 * Let the layout model calculate the correct UIArea
	 * @param layoutable
	 * @return
	 */
    public UIDimension getLayoutablePreferedSize(UIDimension layoutable);
    
    /**
     * Clears any cache the model may have
     * @param container
     */
    public void invalidateCache(Object container);
    
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
    
    public void applyLayout(UIComponent container);
}

package org.middleheaven.ui;



public interface UILayoutModel {

	/**
	 * Let the layout model calculate the correct UIArea
	 * @param layoutable
	 * @return
	 */
    public UIArea getLayoutablePreferedSize(UIArea layoutable);
    
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

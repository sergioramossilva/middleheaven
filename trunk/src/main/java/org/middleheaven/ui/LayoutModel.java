package org.middleheaven.ui;



public interface LayoutModel {

    public UIArea getLayoutablePreferedSize(UIArea layoutable);
    
    public void invalidateCache(Object container);
    
    public void componentAdded(ComponentAggregationEvent event);

    public void componentRemoved(ComponentAggregationEvent event);
}

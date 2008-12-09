package org.middleheaven.ui.layout;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

import org.middleheaven.ui.ComponentAggregationEvent;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.models.UILayoutModel;

public class UIBorderLayoutModel implements UILayoutModel {

	public enum UIBorderLayoutConstraint implements UILayoutConstraint{
		NORTH,
		SOUTH,
		WEST,
		EAST,
		CENTER
	}

	private final Map<UIBorderLayoutConstraint, UIComponent> children = new EnumMap<UIBorderLayoutConstraint, UIComponent>(UIBorderLayoutConstraint.class);

	@Override
	public void componentAdded(ComponentAggregationEvent event) {
		if (UIBorderLayoutConstraint.class.isInstance(event.getConstraint())){
			children.put((UIBorderLayoutConstraint)event.getConstraint(), event.getComponent());
		}
	}

	@Override
	public void componentRemoved(ComponentAggregationEvent event) {
		for (Iterator<Map.Entry<UIBorderLayoutConstraint, UIComponent> > it = this.children.entrySet().iterator(); it.hasNext();){
			Map.Entry< UIBorderLayoutConstraint, UIComponent> entry = it.next();
			if (entry.getValue().equals(event.getComponent())){
				it.remove();
				return;
			}
		}
	}

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
		// simulates Swing BorderLayout
		
		int centerWidth = container.getWidth();
		int centerHeight = container.getHeight();
		int centerX = 0;
		int centerY = 0;
		
		
		UIComponent north = this.children.get(UIBorderLayoutConstraint.NORTH);
		if (north!=null){
			centerHeight -= north.getHeight();
			centerY = north.getHeight();
			
			north.setBounds(0,0, container.getWidth(), north.getHeight());
		}
		
		UIComponent south = this.children.get(UIBorderLayoutConstraint.SOUTH);
		if (south!=null){
			centerHeight -= south.getHeight();
			
			south.setBounds(0,container.getHeight()-south.getHeight(), container.getWidth(), south.getHeight());
		}
		
		UIComponent west = this.children.get(UIBorderLayoutConstraint.WEST);
		if (west!=null){
			centerWidth -= west.getHeight();
			centerX = west.getWidth();
			
			west.setBounds(0,centerY,west.getWidth(), centerHeight);
		}
		
		UIComponent east = this.children.get(UIBorderLayoutConstraint.EAST);
		if (east!=null){
			centerWidth -= east.getHeight();
			
			east.setBounds(container.getWidth()-east.getWidth(),centerY,east.getWidth(), centerHeight);
		}
		
		UIComponent center = this.children.get(UIBorderLayoutConstraint.CENTER);
		if (center!=null){
			
			center.setBounds(centerX,centerY,centerWidth,centerHeight);
		}
	}
}

package org.middleheaven.ui;


public interface UILayout extends UIComponent{

	 public void addChildComponent (UIComponent component, UILayoutConstraint layoutConstrain);
	 public void removeChildComponent (UIComponent component);
	    
}

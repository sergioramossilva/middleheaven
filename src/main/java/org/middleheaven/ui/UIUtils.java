package org.middleheaven.ui;

public class UIUtils {

	public static void center(UIComponent component){
		center(component,component.getUIParent());
	}

	public static void center(UIComponent component, UIComponent relative){
		
		final int x = (relative.getWidth()- component.getWidth())/2;
		final int y = (relative.getHeight()- component.getHeight())/2;
		
		component.setPosition(x, y);
	}
}

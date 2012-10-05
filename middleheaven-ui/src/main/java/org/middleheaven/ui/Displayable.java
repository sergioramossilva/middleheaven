package org.middleheaven.ui;

/**
 * An object that can be positioned in a reference frame
 * and was a resizable size
 */
public interface Displayable {

	
	public UISize getDisplayableSize();
	
	public void setDisplayableSize(UISize size);
	
	public UIPosition getPosition();
	
	
}

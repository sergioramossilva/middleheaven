package org.middleheaven.ui;

/**
 * An object that can be positioned in a reference frame
 * and was a resizable size
 */
public interface Displayable {

	
	public UIDimension getDimension();
	public UIPosition getPosition();
	
    public int getHeight();
    public int getWidth();
    public int getX();
    public int getY();
    
	public void setBounds(int x, int y, int width, int height);

	public void setSize(UIDimension size);
}

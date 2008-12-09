package org.middleheaven.ui;

public interface Displayable {

	
	public UIDimension getDimension();
	public UIPosition getPosition();
	
    public int getHeight();
    public int getWidth();
    public int getX();
    public int getY();
    
	public void setBounds(int x, int y, int width, int height);

	public void setPosition(int x, int y);
	public void setSize(UIDimension size);
}

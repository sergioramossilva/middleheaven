package org.middleheaven.ui;

import java.io.Serializable;

public final class UIPosition implements Serializable{

	private int y;
	private int x;

	public int getX(){
		return x;
	}
	
    public UIPosition(int x, int y) {
		this.y = y;
		this.x = x;
	}

	public int getY(){
    	return y;
    }
    
}

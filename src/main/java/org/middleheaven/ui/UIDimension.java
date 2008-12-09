package org.middleheaven.ui;

import java.io.Serializable;


public class UIDimension implements Serializable {
    
	private int height;
	private int width;

	public UIDimension() {
		this(0,0);
	}
	
	public UIDimension(int width, int height) {
		this.height = height;
		this.width = width;
	}

	public int getHeight(){
		return this.height;
	}
	
    public int getWidth(){
    	return this.width;
    }
}

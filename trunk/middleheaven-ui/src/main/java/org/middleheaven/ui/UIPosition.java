package org.middleheaven.ui;

import java.io.Serializable;

/**
 * Represents the position of a {@link UIComponent} relative to its container.
 */
public final class UIPosition implements Serializable{


	private static final long serialVersionUID = -5527197182068251627L;
	
	private UIDimension y;
	private UIDimension x;

	public static UIPosition pixels(int x, int y){
		return new UIPosition(
				UIDimension.pixels(x),
				UIDimension.pixels(y)
		);
	}
	
	public static UIPosition valueOf(UIDimension x, UIDimension y){
		return new UIPosition(
				x,
				y
		);
	}
	
    private UIPosition(UIDimension x, UIDimension y) {
		this.y = y;
		this.x = x;
	}

	public UIDimension getX(){
		return x;
	}
	
	public UIDimension getY(){
    	return y;
    }
    
}

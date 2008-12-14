package org.middleheaven.ui;

import org.middleheaven.ui.rendering.RenderKit;


/**
 * The client graphical interface the user interact with in order
 * to input commands and receive informations.
 * 
 */
public interface UIClient extends UIComponent{

	public RenderKit getRenderKit();
	public void setRenderKit(RenderKit renderKit);
}

package org.middleheaven.ui.models;

import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.desktop.awt.UIExitEvent;
import org.middleheaven.ui.rendering.RenderKit;

public interface UIClientModel extends UIModel {

	public RenderKit getRenderKit();
	public void setRenderKit(RenderKit renderKit);
	
	
}

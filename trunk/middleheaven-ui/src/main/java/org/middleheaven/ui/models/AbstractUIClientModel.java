package org.middleheaven.ui.models;

import org.middleheaven.ui.AbstractUIModel;
import org.middleheaven.ui.rendering.RenderKit;

public abstract class AbstractUIClientModel extends AbstractUIModel implements UIClientModel{

	private RenderKit renderKit;
	
	public RenderKit getRenderKit(){
		return this.renderKit;
	}
	
	public void setRenderKit(RenderKit renderKit){
		this.renderKit = renderKit;
	}
}

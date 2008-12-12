package org.middleheaven.ui;

import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.rendering.RenderKit;


public class UIEnvironment extends GenericUIComponent implements UIContainer {

	private RenderKit renderKit;
	
	public UIEnvironment() {
		super(UIEnvironment.class, null);
	}

	public UIEnvironment(String name) {
		super(UIEnvironment.class, null);
		this.setGID(name);
	}

	public RenderKit getRenderKit() {
		return renderKit;
	}

	public void setRenderKit(RenderKit renderKit) {
		this.renderKit = renderKit;
	}
	
	public boolean isRendered(){
		return true;
	}
	
}

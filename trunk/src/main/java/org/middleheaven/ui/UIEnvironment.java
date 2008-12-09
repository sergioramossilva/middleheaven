package org.middleheaven.ui;

import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.rendering.RenderKit;


public class UIEnvironment extends GenericUIComponent implements UIContainer {

	private RenderKit renderKit;
	
	public UIEnvironment() {
		super(UIClient.class, null);
	}

	public UIEnvironment(String name) {
		super(UIClient.class, null);
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

package org.middleheaven.ui;

import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.rendering.RenderType;


public class UIEnvironment extends GenericUIComponent implements UIContainer {

	private RenderKit renderKit;
	
	public UIEnvironment() {
		super(RenderType.ROOT, null);
	}

	public UIEnvironment(String name) {
		super(RenderType.ROOT, null);
		this.setID(name);
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

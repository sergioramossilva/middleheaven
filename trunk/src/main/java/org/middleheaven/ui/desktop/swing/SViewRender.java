package org.middleheaven.ui.desktop.swing;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

public class SViewRender extends UIRender {

	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent,UIComponent component) {

		if ("innerframe".equals(component.getFamily())){
			return new SInternalFrameView();
			
		} else {
			return new SPanelView();
		}
		
	}

}

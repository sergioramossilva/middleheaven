package org.middleheaven.ui.rendering;

import org.middleheaven.ui.UIComponent;

public interface UIDecorator {

	
	public UIComponent decorate(RenderingContext context,UIComponent component);
}

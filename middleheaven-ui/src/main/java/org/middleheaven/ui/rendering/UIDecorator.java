package org.middleheaven.ui.rendering;

import org.middleheaven.ui.UIComponent;

/**
 * Allows for a {@link UIComponent} to be decorated to another component.
 */
public interface UIDecorator {

	
	public UIComponent decorate(RenderingContext context,UIComponent component);
}

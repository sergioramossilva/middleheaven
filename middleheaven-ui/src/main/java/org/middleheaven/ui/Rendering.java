/**
 * 
 */
package org.middleheaven.ui;

import org.middleheaven.ui.rendering.RenderKit;

/**
 * 
 */
public interface Rendering<U extends UIComponent> {

	
	public U getComponent();
	
	public RenderKit getRenderKit();
}

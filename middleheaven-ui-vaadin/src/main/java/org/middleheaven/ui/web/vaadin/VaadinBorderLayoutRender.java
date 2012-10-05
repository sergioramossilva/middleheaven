/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * 
 */
public class VaadinBorderLayoutRender extends AbstractVaadinRender {

	private static final long serialVersionUID = -1576770399958434171L;

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected VaadinUIComponent buildVaadin(RenderingContext context, UIComponent parent, UIComponent component) {
		
		VaadinBorderLayout layout = new VaadinBorderLayout();
		
		
		return layout;
	}

}

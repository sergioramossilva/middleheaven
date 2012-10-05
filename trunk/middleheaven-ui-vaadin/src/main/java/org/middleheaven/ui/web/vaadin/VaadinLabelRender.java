/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * 
 */
public class VaadinLabelRender extends AbstractVaadinRender {


	private static final long serialVersionUID = -4657641168338371998L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected VaadinUIComponent buildVaadin(RenderingContext context, UIComponent parent,
			UIComponent component) {
	
		VaadinLabel label = new VaadinLabel();
		
		return label;
	}

}

/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * 
 */
public class VaadinTabsLayoutRender extends AbstractVaadinRender {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1840024126382924305L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected VaadinUIComponent buildVaadin(RenderingContext context, UIComponent parent,
			UIComponent component) {
	
	
		return new VaadinTabSheetLayout();
		
	}

}

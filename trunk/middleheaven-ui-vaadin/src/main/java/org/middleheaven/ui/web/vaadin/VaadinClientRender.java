/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.process.web.server.ServletWebContext;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

/**
 * 
 */
public class VaadinClientRender extends UIRender {


	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent,
			UIComponent component) {
		
		ServletWebContext scontext = context.getAttribute("mhRequestResponseWebContext", ServletWebContext.class);
		
		return new VaadinClientApplication(scontext, context.getRenderKit().getSceneNavigator());

	}

}

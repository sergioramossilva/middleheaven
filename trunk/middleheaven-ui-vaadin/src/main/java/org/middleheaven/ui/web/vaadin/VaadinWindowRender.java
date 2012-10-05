/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import java.util.Locale;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * 
 */
public class VaadinWindowRender extends AbstractVaadinRender {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected VaadinUIComponent buildVaadin(RenderingContext context, UIComponent parent,
			UIComponent component) {
		

		VaadinWindow window = new VaadinWindow(Locale.US); 

		return window;
	}

}

/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 */
class VaadinFlowLayoutRender extends AbstractVaadinRender {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3205963842204366139L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected VaadinUIComponent buildVaadin(RenderingContext context, UIComponent parent,
			UIComponent component) {
		
		String family = component.getFamily();
		
		if ("flow:vertical".equals(family)){
			return new VaadinFlowLayout(new VerticalLayout());
		} else {
			return new VaadinFlowLayout(new HorizontalLayout());
		}
		
		
	
	}

}

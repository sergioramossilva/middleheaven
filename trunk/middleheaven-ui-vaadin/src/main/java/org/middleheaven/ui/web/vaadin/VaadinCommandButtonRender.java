/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

import com.vaadin.ui.Button;

/**
 * 
 */
public class VaadinCommandButtonRender extends AbstractVaadinRender {


	private static final long serialVersionUID = -6503686577233772775L;

	public VaadinCommandButtonRender (){
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected VaadinUIComponent buildVaadin(RenderingContext context, UIComponent parent,
			UIComponent component) {
		
		
		Button discardChanges = new Button("Discard changes");
		

        return new VaadinButton(discardChanges);

	}

}

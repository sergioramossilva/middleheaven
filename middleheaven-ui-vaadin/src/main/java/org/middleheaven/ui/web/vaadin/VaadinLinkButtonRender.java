/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

import com.vaadin.ui.Button;
import com.vaadin.ui.themes.BaseTheme;

/**
 * 
 */
public class VaadinLinkButtonRender extends AbstractVaadinRender {


	private static final long serialVersionUID = -6503686577233772775L;

	public VaadinLinkButtonRender (){
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected VaadinUIComponent buildVaadin(RenderingContext context, UIComponent parent,
			UIComponent component) {
		
		
		Button discardChanges = new Button("Discard changes");
		
        discardChanges.setStyleName(BaseTheme.BUTTON_LINK);
        
        return new VaadinButton(discardChanges);

	}

}

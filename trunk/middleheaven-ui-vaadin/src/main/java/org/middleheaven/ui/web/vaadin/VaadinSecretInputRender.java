/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;

import com.vaadin.ui.PasswordField;

/**
 * 
 */
public class VaadinSecretInputRender extends AbstractVaadinRender {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7599970618804979823L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected VaadinUIComponent buildVaadin(RenderingContext context, UIComponent parent,UIComponent component) {
	
		PasswordField editor = new PasswordField();

		return new VaadinSecretInput(editor);

	}

}

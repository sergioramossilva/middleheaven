/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.components.UISecretField;

import com.vaadin.ui.Component;

/**
 * 
 */
public class VaadinSecretInput extends VaadinFieldUIComponent implements UISecretField {

	/**
	 * Constructor.
	 * @param component 
	 * @param component
	 * @param type
	 */
	public VaadinSecretInput(Component component) {
		super(component, UISecretField.class);
	}


	
}

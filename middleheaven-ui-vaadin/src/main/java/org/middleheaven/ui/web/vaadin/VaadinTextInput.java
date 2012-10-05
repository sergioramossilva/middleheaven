/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.components.UITextField;

import com.vaadin.ui.Component;

/**
 * 
 */
public class VaadinTextInput extends VaadinFieldUIComponent implements UITextField {

	/**
	 * Constructor.
	 * @param component 
	 * @param component
	 * @param type
	 */
	public VaadinTextInput(Component component) {
		super(component, UITextField.class);
	}

	
}

/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.components.UITextField;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.TextField;

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
	public VaadinTextInput(TextField component) {
		super(component, UITextField.class);
		
		
		component.addListener(new TextChangeListener(){

			@Override
			public void textChange(TextChangeEvent event) {
				getUIModel().setValue(event.getText());
			}
			
		});
		
	}

	
}

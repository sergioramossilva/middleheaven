package org.middleheaven.ui.desktop.swing;

import javax.swing.JFormattedTextField;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UITextInput;

public class STextFieldInput extends SDocumentInput implements UITextInput {

	private static final long serialVersionUID = -8779476270509974866L;
	
	
	
	public STextFieldInput(){
		super(new JFormattedTextField());
	}
	
	@Override
	public <T extends UIComponent> Class<T> getType() {
		return (Class<T>) UITextInput.class;
	}

	

	






}

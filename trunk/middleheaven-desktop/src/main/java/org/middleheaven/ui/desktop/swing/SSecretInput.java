package org.middleheaven.ui.desktop.swing;

import javax.swing.JPasswordField;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UISecretField;

public class SSecretInput extends SDocumentInput implements UISecretField {

	private static final long serialVersionUID = -8779476270509974866L;



	public SSecretInput(){
		super(new JPasswordField());
	}


	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UISecretField.class;
	}









}

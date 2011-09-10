package org.middleheaven.ui.desktop.swing;

import javax.swing.JPasswordField;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UISecretInput;

public class SSecretInput extends SDocumentInput implements UISecretInput {

	private static final long serialVersionUID = -8779476270509974866L;



	public SSecretInput(){
		super(new JPasswordField());
	}


	@Override
	public <T extends UIComponent> Class<T> getType() {
		return (Class<T>) UISecretInput.class;
	}









}

package org.middleheaven.ui.desktop.swing;

import java.awt.ComponentOrientation;

import javax.swing.JFormattedTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UINumericInput;
import org.middleheaven.ui.models.UINumericInputModel;

public class SNumericFieldInput extends SDocumentInput implements UINumericInput {

	private static final long serialVersionUID = -8779476270509974866L;
	
	public SNumericFieldInput(){
		super(new JFormattedTextField());
	
	}
	
	protected void setupTextField(JTextComponent text){
		text.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	}

	@Override
	public <T extends UIComponent> Class<T> getType() {
		return (Class<T>) UINumericInput.class;
	}
	
	public Document getDocument(Document current){
		boolean isDecimal = false;
		try{
			isDecimal = this.getUIModel().getDecimalDigits()>0;
		} catch (NullPointerException e){
			
		}
		((AbstractDocument)current).setDocumentFilter(new NumericTextFilter(isDecimal));
		return current;
	}
	
	public UINumericInputModel getUIModel(){
		return (UINumericInputModel)super.getUIModel();
	}
}

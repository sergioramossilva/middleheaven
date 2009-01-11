package org.middleheaven.ui.desktop.swing;

import javax.swing.BoxLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.middleheaven.global.text.Formatter;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.components.UIFieldInput;
import org.middleheaven.ui.models.UIFieldInputModel;

public abstract class SDocumentInput extends SBaseFieldInput implements UIFieldInput{
	

	private static final long serialVersionUID = -1521785089966526395L;


	private JTextComponent txtField;

	public SDocumentInput(JTextComponent txtField){
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.txtField = txtField;
		this.add(txtField);
		
		
	}
	
	protected void setupTextField(JTextComponent text){
		
	}
	
	public  Document getDocument(Document currentDocument){
		return currentDocument;
	}

	private void setupDocument(){
		
		Document document = getDocument(this.txtField.getDocument());
		
		// if is not the same
		if (txtField.getDocument() != document){
			// change
			txtField.setDocument(document);
		}
		
		txtField.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent event) {
				getUIModel().setValue(format(txtField.getText()));
			}

			@Override
			public void insertUpdate(DocumentEvent event) {
				getUIModel().setValue(format(txtField.getText()));
			}

			@Override
			public void removeUpdate(DocumentEvent event) {
				getUIModel().setValue(format(txtField.getText()));
			}
			
		});
	}
	
	protected Object format(String raw){
		return  raw;
	}
	
	@Override
	public void setUIModel(UIModel model) {
		super.setUIModel((UIFieldInputModel) model);
		setupDocument();
		setupTextField(txtField);
		this.txtField.setText((String)this.getUIModel().getValue());
	}
	
	@Override
	public void gainFocus() {
		txtField.requestFocus();
	}
	
	@Override
	public boolean hasFocus() {
		return txtField.hasFocus();
	}

	@Override
	public boolean isRendered() {
		return true;
	}
	
	
	
}

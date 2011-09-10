package org.middleheaven.ui.desktop.swing;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;

import org.middleheaven.global.text.Formatter;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.components.UIDateInput;
import org.middleheaven.ui.models.UIFieldInputModel;

public class SDateFieldInput extends SDocumentInput implements UIDateInput {

	private static final long serialVersionUID = -8779476270509974866L;
	Formatter ff;
	
	public SDateFieldInput(){
		super(new JFormattedTextField());
	
	}
	
	protected void setupTextField(JTextComponent text){
		((JFormattedTextField)text).setFormatterFactory(new AbstractFormatterFactory(){

			@Override
			public AbstractFormatter getFormatter(JFormattedTextField txt) {
				if (ff!=null){
					return new AbstractFormatterAdapter(ff);
				} else {
					try {
						return new MaskFormatter("##/##/####");
					} catch (ParseException e) {
						return null;
					}
				}
			}
			
		});
	}
	
	public void setUIModel(UIModel model){
		ff = ((UIFieldInputModel)model).getFormater();
		super.setUIModel(model);
		
	}
	
	protected Object format(String raw){
		
		try {
			return  ff!=null ? ff.format(raw) : new SimpleDateFormat("dd/MM/yyyy").parse(raw);
		} catch (ParseException e) {
			return raw;
		}
	}

	@Override
	public <T extends UIComponent> Class<T> getType() {
		return (Class<T>) UIDateInput.class;
	}
	

}

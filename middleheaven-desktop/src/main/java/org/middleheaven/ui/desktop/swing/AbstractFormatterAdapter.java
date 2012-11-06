package org.middleheaven.ui.desktop.swing;

import java.text.ParseException;

import javax.swing.JFormattedTextField.AbstractFormatter;

import org.middleheaven.global.text.ParsableFormatter;

public class AbstractFormatterAdapter extends AbstractFormatter {

	ParsableFormatter formatter;
	
	public AbstractFormatterAdapter(ParsableFormatter formatter) {
		super();
		this.formatter = formatter;
	}


	
	@Override
	public Object stringToValue(String text) throws ParseException {
		return formatter.parse(text);
	}

	@Override
	public String valueToString(Object value) throws ParseException {
		return formatter.format(value);
	}

}

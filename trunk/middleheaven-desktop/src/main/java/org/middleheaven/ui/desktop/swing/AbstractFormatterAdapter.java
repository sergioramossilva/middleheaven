package org.middleheaven.ui.desktop.swing;

import java.text.ParseException;

import javax.swing.JFormattedTextField.AbstractFormatter;

import org.middleheaven.global.text.Formatter;

public class AbstractFormatterAdapter extends AbstractFormatter {

	Formatter formatter;
	
	public AbstractFormatterAdapter(Formatter formatter) {
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

package org.middleheaven.ui.desktop.swing;

import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

public class NumericTextFilter extends DocumentFilter {



	Pattern pattern;
	public NumericTextFilter(boolean isDecimal) {
		if (isDecimal){
			pattern = Pattern.compile("\\d+\\.?\\d*");
		} else {
			pattern = Pattern.compile("^[0-9]*$"); // integer
		}
	}

	public void insertString(DocumentFilter.FilterBypass fb, 
			int offset, String string, AttributeSet attr) 
	throws BadLocationException {   

		if (string == null) {
			return;
		} else {
			replace(fb, offset, 0, string, attr);
		}
	}   

	public void remove(DocumentFilter.FilterBypass fb, 
			int offset, int length)
	throws BadLocationException {

		replace(fb, offset, length, "", null);
	}

	public void replace(DocumentFilter.FilterBypass fb, 
			int offset, int length, String text, AttributeSet attrs) 
	throws BadLocationException {   

		Document doc = fb.getDocument();
		int currentLength = doc.getLength();
		String currentContent = doc.getText(0, currentLength);
		String before = currentContent.substring(0, offset);
		String after = currentContent.substring(length+offset, currentLength);
		String newValue = before.concat(text == null ? "" : text).concat(after);
		checkInput(newValue, offset);
		fb.replace(offset, length, text, attrs);
	}

	private void checkInput(String proposedValue, int offset)throws BadLocationException {

		if (proposedValue.length() > 0) {

			if (!pattern.matcher(proposedValue).matches()){
				throw new BadLocationException(proposedValue, offset);
			}

		}

	}
}


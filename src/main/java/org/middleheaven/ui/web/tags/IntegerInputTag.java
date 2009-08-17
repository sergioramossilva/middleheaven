package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspTagException;



public class IntegerInputTag extends InputTag{

	public IntegerInputTag() {
		super("TEXTFIELD");
		this.setSubtype("digits");
	}
	
	protected void writeInputOption() throws JspTagException {
		
		// signals to the form that this is a integer field enabled form
		FormTag form = this.findAncestorTag(FormTag.class);
		form.setHasIntegerFields(true);
		
		super.writeInputOption();

	}

}

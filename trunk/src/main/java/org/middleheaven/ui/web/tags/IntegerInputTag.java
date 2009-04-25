package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspTagException;



public class IntegerInputTag extends InputTag{

	public IntegerInputTag() {
		super("TEXTFIELD");
		this.setSubtype("digits");
	}
	
	protected void writeInputOption() throws JspTagException {
		
		// sinaliza o formulário que se trata de um formulario com campos inteiros
		FormTag form = this.findAncestorTag(FormTag.class);
		form.setHasIntegerFields(true);
		
		super.writeInputOption();

	}

}

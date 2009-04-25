package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspTagException;




public class DecimalInputTag extends InputTag{

	public DecimalInputTag() {
		super("TEXTFIELD");
		this.setSubtype("number");
	}
	
	protected void writeInputOption() throws JspTagException {
		
		// sinaliza o formulário que se trata de um formulario com campos decimais
		FormTag form = this.findAncestorTag(FormTag.class);
		form.setHasDecimalFields(true);
		
		super.writeInputOption();

	}
}

package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspTagException;


public class UploadInputTag extends InputTag{

	public UploadInputTag() {
		super("FILE");
	}

	protected void writeInputOption() throws JspTagException {

		// sinaliza o formulário que se trata de um formulario de upload
		FormTag form = this.findAncestorTag(FormTag.class);
		form.isUpload= true;
	}

}

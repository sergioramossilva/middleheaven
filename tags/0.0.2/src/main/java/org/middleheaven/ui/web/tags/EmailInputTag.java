package org.middleheaven.ui.web.tags;



public class EmailInputTag extends TextInputTag{

	public EmailInputTag() {
		this.setMaxLength(255);
		this.setSubtype("email");
	}

}

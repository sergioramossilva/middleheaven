package org.middleheaven.ui.web.tags;



public class URLInputTag extends TextInputTag{

	public URLInputTag() {
		this.setMaxLength(255);
		this.setSubtype("url");
	}

}

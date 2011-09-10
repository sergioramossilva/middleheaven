package org.middleheaven.ui.web;

import org.middleheaven.ui.models.AbstractUIClientModel;

public class BrowserClientModel extends AbstractUIClientModel {

	public BrowserClientModel(){
		this.setEnabled(true);
		this.setRenderKit(new HtmlRenderKit());
	}
	
}

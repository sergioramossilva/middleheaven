package org.middleheaven.ui.web;

import org.middleheaven.ui.AbstractUIClient;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.components.UIBrowser;

public class Browser extends AbstractUIClient implements UIBrowser{

	
	
	public Browser() {
		super(new HTMLRenderKit());
	}

	@Override
	public UIDimension getDimension() {
		// TODO Auto-generated method stub
		return null;
	}



}

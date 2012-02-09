package org.middleheaven.ui.web;

import org.middleheaven.ui.AbstractUIClient;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.components.UIBrowser;

public class Browser extends AbstractUIClient implements UIBrowser{

	
	
	public Browser() {
	
	}

	@Override
	public UIDimension getDimension() {
		return UIDimension.of(0, 0);
	}

	@Override
	public void exit() {
		// no-op
	}



}
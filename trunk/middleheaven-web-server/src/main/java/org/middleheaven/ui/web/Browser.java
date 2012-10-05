package org.middleheaven.ui.web;

import org.middleheaven.ui.AbstractUIClient;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.components.UIBrowser;

public class Browser extends AbstractUIClient implements UIBrowser{

	
	
	public Browser() {
	
	}

	@Override
	public UISize getDisplayableSize() {
		return UISize.pixels(0, 0);
	}

	@Override
	public void exit() {
		// no-op
	}
	
    public boolean isRendered(){
    	return true;
    }

}

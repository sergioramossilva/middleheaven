package org.middleheaven.ui.desktop.swing;

import org.middleheaven.ui.components.UITitledUIModel;
import org.middleheaven.ui.components.UIView;

public class SView extends SBasePanel implements UIView {


	private static final long serialVersionUID = 1L;

	
	public UITitledUIModel getUIModel(){
		return (UITitledUIModel)super.getUIModel();
	}
	
	@Override
	public String getTitle() {
		return getUIModel().getTitle();
	}

}

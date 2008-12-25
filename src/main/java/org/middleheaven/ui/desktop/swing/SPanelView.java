package org.middleheaven.ui.desktop.swing;

import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.components.UITitledUIModel;
import org.middleheaven.ui.components.UIView;

public class SPanelView extends SBasePanel implements UIView {


	private static final long serialVersionUID = 1L;

	public void setUIModel(UIModel model){
		super.setUIModel((UITitledUIModel)model);
		
	}
	
	public UITitledUIModel getUIModel(){
		return (UITitledUIModel)super.getUIModel();
	}
	

}

package org.middleheaven.ui.desktop.swing;

import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.components.UIView;
import org.middleheaven.ui.models.UIViewModel;

public class SPanelView extends SBasePanel implements UIView {


	private static final long serialVersionUID = 1L;

	public void setUIModel(UIModel model){
		super.setUIModel((UIViewModel)model);
		
	}
	
	public UIViewModel getUIModel(){
		return (UIViewModel)super.getUIModel();
	}
	

}

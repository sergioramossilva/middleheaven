package org.middleheaven.ui.components;

import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.models.UIInputModel;

public interface UIInput extends UIOutput {

	public UIInputModel getUIModel();
	public void setReadState(UIReadState state);

}

package org.middleheaven.ui.desktop.swing;

import javax.swing.BoxLayout;

import org.middleheaven.ui.components.UIFieldInput;
import org.middleheaven.ui.models.UIFieldInputModel;

public abstract class SBaseFieldInput extends SBaseInput implements UIFieldInput {
	

	private static final long serialVersionUID = 1L;

	
	public SBaseFieldInput(){
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
	}
	
	@Override
	public UIFieldInputModel getUIModel() {
		return (UIFieldInputModel)super.getUIModel();
	}
	

	@Override
	public boolean isRendered() {
		return true;
	}
	
	
}

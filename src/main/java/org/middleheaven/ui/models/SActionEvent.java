package org.middleheaven.ui.models;

import org.middleheaven.ui.UIComponent;

public class SActionEvent implements UIActionEvent {

	private UIComponent source;
	
	public SActionEvent(UIComponent source){
		this.source = source;
	}
	
	@Override
	public UIComponent getSource() {
		return source;
	}

}

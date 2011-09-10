package org.middleheaven.ui.events;

import org.middleheaven.ui.UIComponent;

public abstract class UIEvent {

	private UIComponent source;
	
	public UIEvent(UIComponent source){
		this.source = source;
	}
	
	public UIComponent getSource() {
		return source;
	}
}

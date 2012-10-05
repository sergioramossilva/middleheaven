package org.middleheaven.ui.events;

import org.middleheaven.ui.UIComponent;


public class UIActionEvent extends UIEvent {

	private String name;

	public UIActionEvent(String name, UIComponent source) {
		super(source);
		this.name = name;
	}

	public String getName(){
		return name;
	}
	
}

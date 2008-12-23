package org.middleheaven.ui.desktop.swing;

import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.ui.NamingContainer;
import org.middleheaven.ui.UIComponent;

public class SLayout extends SBasePanel implements NamingContainer {

	Map<String, UIComponent> childMap = new TreeMap<String, UIComponent>();
	
	public void addComponent(UIComponent c){
		c.setUIParent(this);
		this.childMap.put(c.getGID(), c);
		
		if (this.getComponentCount() == 0 || this.getComponent(0) instanceof UIComponent){
			super.addComponent(c);
		}
	}
	
	@Override
	public UIComponent findContainedComponent(String componentID) {
		return childMap.get(componentID);
	}

}

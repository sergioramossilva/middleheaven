package org.middleheaven.ui.desktop.swing;

import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.ui.NamingContainer;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.models.UILayoutModel;

public class SLayout extends SBasePanel implements NamingContainer , UILayout{


	private static final long serialVersionUID = 7689224212258618032L;
	
	Map<String, UIComponent> childMap = new TreeMap<String, UIComponent>();
	
	public void addComponent(UIComponent c){
		c.setUIParent(this);
		this.childMap.put(c.getGID(), c);
		
		if (this.getComponentCount() == 0 || this.getComponent(0) instanceof UIComponent){
			super.addComponent(c);
		}
	}
	
	public UILayoutModel getUIModel(){
		return (UILayoutModel) super.getUIModel();
	}
	
	@Override
	public UIComponent findContainedComponent(String componentID) {
		return childMap.get(componentID);
	}

	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UILayout.class;
	}

	@Override
	public void addComponent(UIComponent component,
			UILayoutConstraint layoutConstrain) {
		addComponent(component);	
		
	}

}

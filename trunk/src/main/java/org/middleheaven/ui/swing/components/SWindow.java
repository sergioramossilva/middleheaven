package org.middleheaven.ui.swing.components;

import java.util.Set;

import javax.swing.JFrame;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.UIQuery;
import org.middleheaven.ui.rendering.RenderType;

public class SWindow extends JFrame implements UIComponent{

	private UIComponent parent;
	private String id;
	
	
	@Override
	public void addChildComponent(UIComponent component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<UIComponent> findComponents(UIQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void gainFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<UIComponent> getChildrenComponents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getChildrenCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getFamily() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public RenderType getType() {
		return RenderType.WINDOW;
	}

	@Override
	public UIModel getUIModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UIComponent getUIParent() {
		return parent;
	}

	@Override
	public boolean isRendered() {
		return true;
	}

	@Override
	public void removeChildComponent(UIComponent component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFamily(String family) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setID(String id) {
		this.id = id;
	}

	@Override
	public void setUIModel(UIModel model) {
		
	}

	@Override
	public void setUIParent(UIComponent parent) {
		this.parent = parent;
	}

}

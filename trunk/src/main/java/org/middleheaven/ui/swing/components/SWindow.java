package org.middleheaven.ui.swing.components;

import java.util.List;
import java.util.Set;

import javax.swing.JFrame;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.UIQuery;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.components.UIWindow;

public class SWindow extends JFrame implements UIComponent{

	private UIComponent parent;
	private String id;
	
	
	@Override
	public void addComponent(UIComponent component) {
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
	public List<UIComponent> getChildrenComponents() {
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
	public String getGID() {
		return id;
	}

	@Override
	public <T extends UIComponent> Class<T> getType() {
		return (Class<T>) UIWindow.class;
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
	public void removeComponent(UIComponent component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFamily(String family) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGID(String id) {
		this.id = id;
	}

	@Override
	public void setUIModel(UIModel model) {
		
	}

	@Override
	public void setUIParent(UIComponent parent) {
		this.parent = parent;
	}

	@Override
	public void setPosition(int x, int y) {
		this.setBounds(x, y, this.getWidth(), this.getHeight());
	}

	@Override
	public UIPosition getPosition() {
		return new UIPosition(this.getX(),this.getY());
	}

	@Override
	public void setSize(UIDimension size) {
		this.setSize(size.getWidth(), size.getHeight());
	}

	@Override
	public UIDimension getDimension() {
		return new UIDimension(this.getWidth(), this.getHeight());
	}

}

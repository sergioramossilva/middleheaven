package org.middleheaven.ui.desktop.swing;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.components.UITitledUIModel;
import org.middleheaven.ui.components.UIView;
import org.middleheaven.util.DelegatingList;

public class SInternalFrameView extends JInternalFrame implements UIView{

	private String family;
	private String id;
	private UITitledUIModel model;
	private UIComponent parent;

	@Override
	public void setUIModel(UIModel model) {
		this.model = (UITitledUIModel)model;
	}
	
	@Override
	public UITitledUIModel getUIModel() {
		return model;
	}

	@Override
	public void addComponent(UIComponent component) {
		component.setUIParent(this);
		this.getContentPane().add((JComponent)component);
	}
	
	
	@Override
	public void removeComponent(UIComponent component) {
		this.getContentPane().remove((JComponent)component);
	}

	@Override
	public void gainFocus() {
		this.requestFocus();
	}

	@Override
	public List<UIComponent> getChildrenComponents() {
		return new DelegatingList<UIComponent>(){

			@Override
			public UIComponent get(int index) {
				return (UIComponent)getContentPane().getComponent(index);
			}

			@Override
			public int size() {
				return getContentPane().getComponentCount();
			}
			
		};
	}

	@Override
	public int getChildrenCount() {
		return this.getContentPane().getComponentCount();
	}

	@Override
	public String getFamily() {
		return family;
	}

	@Override
	public String getGID() {
		return id;
	}

	@Override
	public <T extends UIComponent> Class<T> getType() {
		return (Class<T>) UIView.class;
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
	public void setFamily(String family) {
		this.family = family;
	}

	@Override
	public void setGID(String id) {
		this.id = id;
	}



	@Override
	public void setUIParent(UIComponent parent) {
		this.parent = parent;
	}

	@Override
	public UIDimension getDimension() {
		// TODO implement Displayable.getDimension
		return null;
	}

	@Override
	public UIPosition getPosition() {
		// TODO implement Displayable.getPosition
		return null;
	}

	@Override
	public void setPosition(int x, int y) {
		// TODO implement Displayable.setPosition
		
	}

	@Override
	public void setSize(UIDimension size) {
		// TODO implement Displayable.setSize
		
	}

}

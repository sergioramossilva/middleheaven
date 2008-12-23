package org.middleheaven.ui.desktop.swing;

import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.UIQuery;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.components.UIWindow;
import org.middleheaven.ui.components.UIWindowModel;
import org.middleheaven.util.DelegatingList;

public class SWindow extends JFrame implements UIComponent{

	private UIComponent parent;
	private String id;
	private String family;
	private UIWindowModel model;
	
	@Override
	public Collection<UIComponent> findComponents(UIQuery query) {
		return query.execute(this);
	}
	
	@Override
	public void addComponent(UIComponent component) {
		component.setUIParent(this);
		if (component instanceof JMenuBar){
			this.setJMenuBar((JMenuBar)component);
		} else {
			this.getContentPane().add((JComponent)component);
		}
	}

	@Override
	public void removeComponent(UIComponent component) {
		this.getContentPane().remove((JComponent)component);
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
				return getComponentCount();
			}
			
		};
	}

	@Override
	public int getChildrenCount() {
		return getComponentCount();
	}
	
	@Override
	public void gainFocus() {
		this.requestFocus();
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
		return (Class<T>) UIWindow.class;
	}

	@Override
	public UIWindowModel getUIModel() {
		return model;
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
	public void setUIModel(UIModel model) {
		this.model = (UIWindowModel) model;
		this.setTitle(this.model.getTitle());
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

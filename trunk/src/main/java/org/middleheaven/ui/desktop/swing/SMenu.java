package org.middleheaven.ui.desktop.swing;

import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenu;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.UIQuery;
import org.middleheaven.ui.components.UILabel;
import org.middleheaven.ui.models.UICommandModel;
import org.middleheaven.util.DelegatingList;

public class SMenu extends JMenu implements UIComponent {

	private static final long serialVersionUID = 1L;
	
	private String family;
	private String id;
	private UICommandModel model;
	private UIComponent parent;

	public SMenu(){}

	@Override
	public void gainFocus() {
		this.requestFocus();
	}
	
	@Override
	public Collection<UIComponent> findComponents(UIQuery query) {
		return query.execute(this);
	}
	
	@Override
	public void addComponent(UIComponent component) {
		component.setUIParent(this);
		this.add((JComponent)component);
	}

	@Override
	public void removeComponent(UIComponent component) {
		this.remove((JComponent)component);
	}
	
	@Override
	public List<UIComponent> getChildrenComponents() {
		return new DelegatingList<UIComponent>(){

			@Override
			public UIComponent get(int index) {
				return (UIComponent)getComponent(index);
			}

			@Override
			public int size() {
				return getComponentCount();
			}
			
		};
	}


	@Override
	public int getChildrenCount() {
		return this.getComponentCount();
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
		return (Class<T>) UILabel.class;
	}

	@Override
	public UIModel getUIModel() {
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
		this.id= id;
	}

	@Override
	public void setUIModel(UIModel model) {
		this.model = (UICommandModel) model;
		this.setText(this.model.getText());
	}

	@Override
	public void setUIParent(UIComponent parent) {
		this.parent = parent;
	}

	@Override
	public UIDimension getDimension() {
		return new UIDimension(this.getWidth(), this.getHeight());
	}

	@Override
	public UIPosition getPosition() {
		return new UIPosition(this.getX(),this.getY());
	}

	@Override
	public void setPosition(int x, int y) {
		this.setBounds(x, y, this.getWidth(), this.getHeight());
	}

	@Override
	public void setSize(UIDimension size) {
		this.setBounds(this.getX(), this.getY(), size.getWidth(), size.getHeight());
	}



}

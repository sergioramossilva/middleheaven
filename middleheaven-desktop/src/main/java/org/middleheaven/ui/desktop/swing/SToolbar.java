package org.middleheaven.ui.desktop.swing;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JToolBar;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.components.UICommandSet;
import org.middleheaven.ui.models.UICommandModel;
import org.middleheaven.util.collections.DelegatingList;

public class SToolbar extends JToolBar implements UICommandSet{


	private static final long serialVersionUID = -2053844931546150045L;
	
	private String family;
	private String id;
	private UICommandModel model;
	private UIComponent parent;
	
	public SToolbar(){
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType(Class<? extends UIComponent> type) {
		return type.isAssignableFrom(this.getComponentType());
	}
	
	@Override
	public UICommandModel getUIModel() {
		return model;
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
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UICommandSet.class;
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
		this.model = (UICommandModel)model;
	}

	@Override
	public void setUIParent(UIComponent parent) {
		this.parent = parent;
	}

	@Override
	public UISize getDisplayableSize() {
		return UISize.pixels(this.getWidth(), this.getHeight());
	}

	@Override
	public UIPosition getPosition() {
		return UIPosition.pixels(this.getX(),this.getY());
	}

	@Override
	public void setDisplayableSize(UISize size) {
		UISize pixelSize =  SwingUnitConverter.getInstance().toPixels(size, this.getUIParent());

		this.setBounds(
				this.getX(), 
				this.getY(), 
				(int)pixelSize.getWidth().getValue(),
				(int)pixelSize.getHeight().getValue()
		);
	}

	
}

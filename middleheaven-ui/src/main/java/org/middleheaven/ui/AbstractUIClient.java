package org.middleheaven.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.middleheaven.ui.models.UIClientModel;

public abstract class AbstractUIClient implements UIClient , NamingContainer {

	private String family;
	private String id;
	private Map<String, UIComponent> components = new LinkedHashMap<String, UIComponent>();
	
	private UIClientModel uiModel;

	public AbstractUIClient(){}
	
	@Override
	public void setUIModel(UIModel model) {
		if (!(model instanceof UIClientModel)){
			throw new IllegalArgumentException("Expected a " + UIClientModel.class + " received " + model.getClass());
		}
		this.uiModel = (UIClientModel)model;
	}
	
	@Override
	public UIClientModel getUIModel() {
		return uiModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType(Class<? extends UIComponent> type) {
		return type.isAssignableFrom(this.getComponentType());
	}
	
	public UIComponent findContainedComponent(String componentID){
		return components.get(componentID);
	}
	
	@Override
	public void addComponent(UIComponent component) {
		component.setUIParent(this);
		components.put(component.getGID(), component);
	}
	
	@Override
	public void removeComponent(UIComponent component) {
		components.remove(component.getGID());
	}

	@Override
	public List<UIComponent> getChildrenComponents() {
		return new ArrayList<UIComponent>(this.components.values());
	}

	@Override
	public int getChildrenCount() {
		return components.size();
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
		return (Class<T>) UIClient.class;
	}


	@Override
	public final UIComponent getUIParent() {
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isRendered() {
		return true;
	}

	@Override
	public boolean isVisible() {
		return true;
	}


	@Override
	public void setEnabled(boolean enabled) {
		//no-op
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
		//no-op
	}

	@Override
	public void setVisible(boolean visible) {
		// no-op
	}

	
	@Override
	public void setDisplayableSize(UISize size) {
		throw new UnsupportedOperationException("Can not change " + this.getClass().getSimpleName() + " size");
	}
	


	@Override
	public UIPosition getPosition() {
		return UIPosition.pixels(0,0);
	}


}

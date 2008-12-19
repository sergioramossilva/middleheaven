package org.middleheaven.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.middleheaven.ui.models.UIClientModel;

public abstract class AbstractUIClient implements UIClient , NamingContainer {

	private String family;
	private String id;
	private Map<String, UIComponent> components = new LinkedHashMap<String, UIComponent>();
	
	private UIClientModel uiModel;

	
	@Override
	public void setUIModel(UIModel model) {
		this.uiModel = (UIClientModel)model;
	}
	
	@Override
	public UIClientModel getUIModel() {
		return uiModel;
	}

	
	public Set<UIComponent> findContainedComponent(String componentID){
		return Collections.singleton(components.get(componentID));
	}
	
	@Override
	public void addComponent(UIComponent component) {
		components.put(component.getGID(), component);
	}
	
	@Override
	public void removeComponent(UIComponent component) {
		components.remove(component.getGID());
	}

	@Override
	public Set<UIComponent> findComponents(UIQuery query) {
		return query.execute(this);
	}

	@Override
	public void gainFocus() {
		//no-op
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
	public <T extends UIComponent> Class<T> getType() {
		return (Class<T>) UIClient.class;
	}


	@Override
	public final UIComponent getUIParent() {
		return null;
	}

	@Override
	public boolean hasFocus() {
		return true;
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
	public int getHeight() {
		return getDimension().getHeight();
	}

	@Override
	public int getWidth() {
		return getDimension().getWidth();
	}
	
	@Override
	public void setSize(UIDimension size) {
		throw new UnsupportedOperationException("Cannto change " + this.getClass().getSimpleName() + " size");
	}
	
	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public UIPosition getPosition() {
		return new UIPosition(0,0);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		throw new UnsupportedOperationException("Cannto change " + this.getClass().getSimpleName() + " bounds");
	}

	@Override
	public void setPosition(int x, int y) {
		throw new UnsupportedOperationException("Cannto change " + this.getClass().getSimpleName() + " position");
	}
}

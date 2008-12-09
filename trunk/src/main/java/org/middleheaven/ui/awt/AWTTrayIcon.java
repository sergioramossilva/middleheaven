package org.middleheaven.ui.awt;

import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.UIQuery;
import org.middleheaven.ui.components.UIDesktopTrayIcon;

public class AWTTrayIcon extends TrayIcon implements UIDesktopTrayIcon{

	private String id;
	private UIComponent parent;
	private UIModel uiModel;
	private String family;
	private boolean enabled = true;
	
	public AWTTrayIcon(Image image, String tooltip, PopupMenu popup) {
		super(image, tooltip, popup);
	}

	@Override
	public void addComponent(UIComponent component) {
		//no-op
	}

	@Override
	public Set<UIComponent> findComponents(UIQuery query) {
		return Collections.emptySet();
	}

	@Override
	public void gainFocus() {
		//no-op
	}

	@Override
	public List<UIComponent> getChildrenComponents() {
		return Collections.emptyList();
	}

	@Override
	public int getChildrenCount() {
		return 1;
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
		return (Class<T>) UIDesktopTrayIcon.class;
	}

	@Override
	public UIModel getUIModel() {
		return this.uiModel;
	}

	@Override
	public UIComponent getUIParent() {
		return parent;
	}

	@Override
	public boolean hasFocus() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
	public void removeComponent(UIComponent component) {
		// no-op
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
		this.uiModel = model;
	}

	@Override
	public void setUIParent(UIComponent parent) {
		this.parent = parent;
	}

	@Override
	public void setVisible(boolean visible) {
		throw new UnsupportedOperationException("TrayIcon visibility is unmodifable");
	}

	@Override
	public UIDimension getDimension() {
		return new UIDimension(this.getSize().width, this.getSize().height);
	}

	@Override
	public int getHeight() {
		return this.getSize().height;
	}
	
	@Override
	public int getWidth() {
		return this.getSize().width;
	}
	
	@Override
	public UIPosition getPosition() {
		return new UIPosition(0,0);
	}



	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPosition(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSize(UIDimension size) {
		// TODO Auto-generated method stub
		
	}

}

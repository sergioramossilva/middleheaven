package org.middleheaven.ui.desktop.swing;

import java.util.Collections;
import java.util.List;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.components.UIInput;
import org.middleheaven.ui.models.UIInputModel;

public abstract class SBaseInput extends SBasePanel implements UIInput  {

	private UIInputModel model;
	private UIReadState state = UIReadState.INPUT_ENABLED;
	
	public SBaseInput(){}
	
	@Override
	public void setReadState(UIReadState state) {
		UIReadState oldState = this.state;
		this.state = state;
		firePropertyChange("readState", oldState, state);
	}

	@Override
	public UIReadState getReadState() {
		return state;
	}


	
	@Override
	public UIInputModel getUIModel() {
		return model;
	}
	
	@Override
	public void setUIModel(UIModel model) {
		this.model = (UIInputModel) model;

	}
	
	@Override
	public void addComponent(UIComponent component) {
		throw new UnsupportedOperationException( this.getClass().getName() + " is a leaf component");
	}
	
	@Override
	public void removeComponent(UIComponent component) {
		throw new UnsupportedOperationException( this.getClass().getName() + " is a leaf component");
	}

	@Override
	public List<UIComponent> getChildrenComponents() {
		return Collections.emptyList();
	}

	@Override
	public int getChildrenCount() {
		return 0;
	}

	@Override
	public boolean isRendered() {
		return true;
	}
}

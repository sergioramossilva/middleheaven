package org.middleheaven.ui.desktop.swing;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.components.UIView;
import org.middleheaven.ui.models.UIViewModel;

public class SPanelView extends SBasePanel implements UIView {


	private static final long serialVersionUID = 1L;

	public void setUIModel(UIModel model){
		super.setUIModel((UIViewModel)model);
		
	}
	
	public UIViewModel getUIModel(){
		return (UIViewModel)super.getUIModel();
	}

	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UIView.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIContainerLayout(UILayout component) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UILayout getUIContainerLayout() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component,
			UILayoutConstraint layoutConstrain) {
		throw new UnsupportedOperationException("Not implememented yet");
	}
	

}

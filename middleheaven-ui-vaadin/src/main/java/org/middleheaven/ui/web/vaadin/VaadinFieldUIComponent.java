/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.components.UIField;
import org.middleheaven.ui.models.UIFieldInputModel;

import com.vaadin.ui.Component;

/**
 * 
 */
public class VaadinFieldUIComponent extends VaadinUIComponent implements UIField {

	/**
	 * Constructor.
	 * @param component
	 * @param type
	 */
	public VaadinFieldUIComponent(Component component,
			Class<? extends UIComponent> type) {
		super(component, type);
	}
	
	UIReadState state;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setReadState(UIReadState state) {
		this.state = state;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIReadState getReadState() {
		return state;
	}

	public UIFieldInputModel getUIModel(){
		return (UIFieldInputModel) super.getUIModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyModel() {

	}

}

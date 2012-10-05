/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.components.UICommandSet;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.models.UICommandModel;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

/**
 * 
 */
public class VaadinCommandSet extends VaadinUIComponentContainer implements UICommandSet {

	/**
	 * Constructor.
	 * @param component
	 * @param type
	 */
	public VaadinCommandSet() {
		super(new Panel(new HorizontalLayout()), UICommandSet.class);
	}

	public UICommandModel getUIModel(){
		return (UICommandModel) super.getUIModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyModel() {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIContainerLayout(UILayout layout) {
		this.layout = layout;
		
		if (layout instanceof VaadinUIComponent){
			Panel component = (Panel) this.getComponent();
			component.setContent((ComponentContainer) ((VaadinUIComponent)layout).getComponent());
		}
	}
}

/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.components.UICommandSet;
import org.middleheaven.ui.components.UIForm;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.models.UIFormModel;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 */
public class VaadinForm extends VaadinUIComponentContainer implements UIForm{

	/**
	 * Constructor.
	 * @param component
	 * @param type
	 */
	public VaadinForm() {
		super(new Panel(new FormLayout()), UIForm.class);
	}

	public VaadinForm(Panel panel) {
		super(panel, UIForm.class);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UICommandSet getCommandSet() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	public UIFormModel getUIModel(){
		return (UIFormModel) super.getUIModel();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyModel() {
		this.getComponent().setSizeFull();
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

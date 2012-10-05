/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.components.UIView;
import org.middleheaven.ui.models.UIViewModel;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;

/**
 * 
 */
public class VaadinView extends VaadinUIComponentContainer implements UIView  {

	/**
	 * Constructor.
	 */
	public VaadinView() {
		super(new Panel(), UIView.class);
	}

	public UIViewModel getUIModel(){
		return (UIViewModel) super.getUIModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyModel() {
		Panel component = (Panel) this.getComponent();
		component.setSizeFull();
		if (this.getUIModel().getTitle() != null){
			component.setCaption(this.localize(this.getUIModel().getTitle()));
		}
		
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

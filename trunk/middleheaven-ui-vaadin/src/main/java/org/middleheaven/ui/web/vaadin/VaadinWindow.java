/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import java.util.Locale;

import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.components.UIWindow;
import org.middleheaven.ui.models.UIWindowModel;

import com.vaadin.Application;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Window;

/**
 * 
 */
public class VaadinWindow extends VaadinUIComponentContainer implements UIWindow {

	/**
	 * Constructor.
	 * @param locale 
	 */
	public VaadinWindow(Locale locale) {
		super(new Window(), UIWindow.class);
		((Window) this.getComponent()).setLocale(locale);
	}
	
	public UIWindowModel getUIModel(){
		return (UIWindowModel) super.getUIModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGID(String id) {
		super.setGID(id);
		((Window)this.getComponent()).setName(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyModel() {
		Window component = (Window) this.getComponent();
		component.setSizeFull();
		component.setCaption(this.localize(this.getUIModel().getTitle()));
	}
	
	
	public void setVisible(boolean visible){
		
		super.setVisible(visible);
		
		Window component = (Window) this.getComponent();
		
		Application app = component.getApplication();
		
		app.setMainWindow(component);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIContainerLayout(UILayout layout) {
		this.layout = layout;
		
		if (layout instanceof VaadinUIComponent){
			Window component = (Window) this.getComponent();
			component.setContent((ComponentContainer) ((VaadinUIComponent)layout).getComponent());
		}
	}
	
}

package org.middleheaven.ui.desktop.swing;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UICommandSet;
import org.middleheaven.ui.components.UIWindow;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

public class SMenuRender extends UIRender {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isChildrenRenderer(RenderingContext context, UIComponent parent,UIComponent component) {
		return true;
	}
	
	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent,UIComponent component) {
		
		UIComponent menu = parent.getType().equals(UIWindow.class) ? 
			new SMenuBar() : 
			new SMenu();

		menu.setFamily("menu");
		
		RenderKit renderKit = context.getRenderKit();
		for (UIComponent comp : component.getChildrenComponents()){

			UIComponent renderedComponent = renderKit.renderComponent(context, menu, comp);

			menu.addComponent(renderedComponent);
		}
		
		return menu;
		
	}

}
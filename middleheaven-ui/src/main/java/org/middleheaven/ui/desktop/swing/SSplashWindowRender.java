package org.middleheaven.ui.desktop.swing;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIWindow;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

public class SSplashWindowRender extends UIRender {

	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent,UIComponent component) {

		UIWindow window =  new SRawWindow();

		return window;
	}

}

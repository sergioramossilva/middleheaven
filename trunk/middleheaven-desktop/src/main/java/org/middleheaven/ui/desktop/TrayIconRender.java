package org.middleheaven.ui.desktop;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

public class TrayIconRender extends UIRender{

	private static final long serialVersionUID = -2940336524997242580L;

	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent,UIComponent component) {
		
		return new AWTTrayIcon(null,null,null);
		
	}

}

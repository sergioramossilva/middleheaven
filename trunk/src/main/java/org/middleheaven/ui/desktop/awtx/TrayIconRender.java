package org.middleheaven.ui.desktop.awtx;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

public class TrayIconRender extends UIRender{

	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent,UIComponent component) {
		
		return new AWTTrayIcon(null,null,null);
		
	}

}

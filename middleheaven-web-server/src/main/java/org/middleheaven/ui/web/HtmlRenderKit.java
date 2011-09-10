package org.middleheaven.ui.web;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.components.UICommandSet;
import org.middleheaven.ui.components.UISelectOne;
import org.middleheaven.ui.rendering.AbstractRenderKit;
import org.middleheaven.ui.rendering.UIRender;
import org.middleheaven.ui.rendering.UIUnitConverter;
import org.middleheaven.ui.web.html.HtmlCommandButtonRender;
import org.middleheaven.ui.web.html.HtmlCommandHiperLinkRender;
import org.middleheaven.ui.web.html.HtmlCommandSetRender;
import org.middleheaven.ui.web.html.HtmlDropDownRender;

public class HtmlRenderKit extends AbstractRenderKit {

	
	public HtmlRenderKit(){
		
		UIRender ddr = new HtmlDropDownRender();
		this.addRender(ddr, UISelectOne.class);
		this.addRender(ddr, UISelectOne.class, "dropdown");
		
		this.addRender(new HtmlCommandSetRender(), UICommandSet.class);
		
		final HtmlCommandButtonRender render = new HtmlCommandButtonRender();
		this.addRender(render, UICommand.class);
		this.addRender(render, UICommand.class, "button");
		
		this.addRender(new HtmlCommandHiperLinkRender(), UICommand.class, "link");
	}
	
	@Override
	public void dispose(UIComponent splash) {
		//no-op
	}

	@Override
	public UIUnitConverter getUnitConverted() {
		// TODO implement RenderKit.getUnitConverted
		return null;
	}

	@Override
	public void show(UIComponent component) {
		component.setVisible(true);
	}


}

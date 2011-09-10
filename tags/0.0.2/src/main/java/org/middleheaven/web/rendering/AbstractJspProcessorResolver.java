package org.middleheaven.web.rendering;

import org.middleheaven.ui.ContextScope;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.web.Browser;
import org.middleheaven.ui.web.BrowserClientModel;
import org.middleheaven.web.processing.action.RequestResponseWebContext;

public abstract class AbstractJspProcessorResolver implements RenderingProcessorResolver{

	
	protected final void injectBrowserClient(RequestResponseWebContext context) {
		// set uiclient only for rendering
		Browser browser = new Browser();
		//TODO detect size from cookies
		browser.setUIModel(new BrowserClientModel());

		browser.setFamily(context.getAgent().getBrowserInfo().getBaseEngine());
		context.setAttribute(ContextScope.REQUEST,UIClient.class.getName(), browser);
	}

}

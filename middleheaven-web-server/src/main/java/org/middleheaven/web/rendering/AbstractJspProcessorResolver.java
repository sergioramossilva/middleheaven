package org.middleheaven.web.rendering;

import org.middleheaven.process.ContextScope;
import org.middleheaven.process.web.server.action.RequestResponseWebContext;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.web.Browser;
import org.middleheaven.ui.web.BrowserClientModel;

/**
 * {@link RenderingProcessorResolver} that delegates to JSP pages.
 */
public abstract class AbstractJspProcessorResolver implements RenderingProcessorResolver{

	/**
	 * 
	 * @param context the running {@link RequestResponseWebContext}.
	 */
	protected final void injectBrowserClient(RequestResponseWebContext context) {
		// set UIClient only for rendering
		Browser browser = new Browser();
		
		//TODO detect display size from cookies and javascript
		
		browser.setUIModel(new BrowserClientModel());
		browser.setFamily(context.getAgent().getBrowserInfo().getBaseEngine());
		
		context.getAttributes().setAttribute(ContextScope.REQUEST,UIClient.class.getName(), browser);
	}

}

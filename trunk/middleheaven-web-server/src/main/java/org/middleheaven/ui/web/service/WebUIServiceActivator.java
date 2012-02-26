package org.middleheaven.ui.web.service;

import org.middleheaven.ui.UIService;
import org.middleheaven.ui.service.AbstractUIServiceActivator;
import org.middleheaven.ui.web.BrowserEnviroment;

public class WebUIServiceActivator extends AbstractUIServiceActivator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void registerEnvironment(UIService uiService) {
		uiService.registerEnvironment(new BrowserEnviroment(), true);
		
	}

	
}

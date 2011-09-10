package org.middleheaven.ui.web.service;

import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.ui.UIService;
import org.middleheaven.ui.service.AbstractUIServiceActivator;
import org.middleheaven.ui.web.BrowserEnviroment;

public class WebUIServiceActivator extends AbstractUIServiceActivator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void registerEnvironment(UIService uiService,
			ActivationContext context) {
		uiService.registerEnvironment(new BrowserEnviroment(), true);
		
	}

	
}

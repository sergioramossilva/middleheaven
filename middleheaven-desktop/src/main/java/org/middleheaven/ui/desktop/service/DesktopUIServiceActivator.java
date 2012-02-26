package org.middleheaven.ui.desktop.service;

import org.middleheaven.ui.DesktopEnvironment;
import org.middleheaven.ui.UIService;
import org.middleheaven.ui.service.AbstractUIServiceActivator;

public class DesktopUIServiceActivator extends AbstractUIServiceActivator {


	public DesktopUIServiceActivator (){
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void registerEnvironment(UIService uiService) {
		uiService.registerEnvironment(new DesktopEnvironment(), true);
	}
}

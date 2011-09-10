package org.middleheaven.ui;

import org.middleheaven.ui.web.Browser;

public class BrowserEnviroment extends UIEnvironment {

	private static final long serialVersionUID = -429821341227290229L;

	public BrowserEnviroment() {
		super(UIEnvironmentType.BROWSER);
	}

	@Override
	protected boolean accept(Class<? extends UIClient> type) {
		return Browser.class.isInstance(type);
	}

}

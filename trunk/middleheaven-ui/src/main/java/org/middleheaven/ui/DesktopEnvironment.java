package org.middleheaven.ui;

import org.middleheaven.ui.components.UIDesktop;

public class DesktopEnvironment extends UIEnvironment {

	public DesktopEnvironment() {
		super(UIEnvironmentType.DESKTOP);
	}

	@Override
	protected boolean accept(Class<? extends UIClient> type) {
		return UIDesktop.class.isInstance(type);
	}

}

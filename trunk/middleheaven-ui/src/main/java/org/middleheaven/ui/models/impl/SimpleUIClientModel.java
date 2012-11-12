package org.middleheaven.ui.models.impl;

import org.middleheaven.process.AttributeContext;
import org.middleheaven.ui.AbstractUIModel;
import org.middleheaven.ui.SceneNavigator;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.models.UIClientModel;

/**
 * 
 */
public class SimpleUIClientModel extends AbstractUIModel implements UIClientModel{

	private boolean splashWindowUsed;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIComponent resolveMainWindow(UIClient client, AttributeContext context) {
		// TODO change definition. lookup famillity or create some qualifier
		return client.getChildrenComponents().get(client.getChildrenCount()-1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIComponent resolveSplashWindow(UIClient client, AttributeContext context) {
		// TODO change definition. lookup famillity or create some qualifier
		return client.getChildrenCount() == 1 ? null : client.getChildrenComponents().get(0);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSplashWindowUsed() {
		return splashWindowUsed;
	}

	/**
	 * Atributes {@link boolean}.
	 * @param splashWindowUsed the splashWindowUsed to set
	 */
	public void setSplashWindowUsed(boolean splashWindowUsed) {
		this.splashWindowUsed = splashWindowUsed;
	}
	
	
}

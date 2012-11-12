package org.middleheaven.ui.models;

import org.middleheaven.process.AttributeContext;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIModel;

/**
 * A specific {@link UIModel} for the {@link UIClient} component.
 */
public interface UIClientModel extends UIModel {

	/**
	 * If the client has ands shows a splash window.
	 * @return <code>true</code> if the client has ands shows a splash window, <code>false</code> otherwise.
	 */
	public boolean isSplashWindowUsed();
	
	/**
	 * 
	 * @return Unrendered UIWindow
	 */
	public abstract UIComponent resolveMainWindow(UIClient client,AttributeContext context);

	/**
	 * Splash window. If unrendered will be rendered 
	 * @param client 
	 * @return splash window.
	 */
	public abstract UIComponent resolveSplashWindow(UIClient client, AttributeContext context);

}

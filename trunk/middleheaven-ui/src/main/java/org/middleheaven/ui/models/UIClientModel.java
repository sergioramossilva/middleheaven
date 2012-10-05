package org.middleheaven.ui.models;

import org.middleheaven.process.AttributeContext;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.rendering.SceneNavigator;

/**
 * A specific {@link UIModel} for the {@link UIClient} component.
 */
public interface UIClientModel extends UIModel {

	/**
	 * The scene navigator.
	 * 
	 * @return the scene navigator.
	 */
	public SceneNavigator getSceneNavigator();
	
	/**
	 * Set the scene navigator.
	 * 
	 */
	public void setSceneNavigator(SceneNavigator sceneNavigator);
	
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

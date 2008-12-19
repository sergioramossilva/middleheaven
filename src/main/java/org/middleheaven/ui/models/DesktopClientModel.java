package org.middleheaven.ui.models;

import org.middleheaven.ui.Context;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIDesktop;
import org.middleheaven.ui.desktop.swing.SwingRenderKit;

/**
 * Desktop based UIClient. Allows to build window based applications.
 * The specific graphic environment (Swing, SWT, ...) is not defined
 * by this class. 
 * 
 * @author Sergio Taborda
 */
public abstract class DesktopClientModel extends AbstractUIClientModel{

	
	public DesktopClientModel(){
		this.setRenderKit(new SwingRenderKit());
	}
	
	/**
	 * 
	 * @return Unrendered UIWindow
	 */
	public abstract UIComponent defineMainWindow(UIDesktop client,Context context);

	/**
	 * Splash window. If unrendered will be rendered 
	 * @param client 
	 * @return splash window.
	 */
	public abstract UIComponent defineSplashWindow(UIDesktop client, Context context);


}

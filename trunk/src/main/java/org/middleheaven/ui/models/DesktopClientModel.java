package org.middleheaven.ui.models;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.progress.BoundProgress;
import org.middleheaven.progress.Progress;
import org.middleheaven.ui.Context;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIEnvironment;
import org.middleheaven.ui.UIService;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * Desktop based UIClient. Allows to build window based applications.
 * The specific graphic environment (Swing, SWT, ...) is not defined
 * by this class. 
 * 
 * @author Sergio Taborda
 */
public abstract class DesktopClientModel implements UIClientModel{

	@Override
	public final void execute(Context context) {

		final UIEnvironment env =  ServiceRegistry.getService(UIService.class)
								.getUIEnvironment(getUIEnvironmentName());
		final RenderKit renderKit = env.getRenderKit();

		UIComponent splash = defineSplashWindow(context);
		RenderingContext renderedContext = new RenderingContext(context);

		Progress progress = new BoundProgress(100); // TODO determine limite

		renderedContext.setAttribute(ContextScope.RENDERING, "progress", progress);

		if (splash!=null){
			if (!splash.isRendered()){
				splash = renderKit.renderComponent(renderedContext, env, splash);
			}
			renderKit.show(splash);
		}

		UIComponent mainWindow = defineMainWindow(context);
		if (!mainWindow.isRendered()){
			mainWindow = renderKit.renderComponent(renderedContext, env, mainWindow);
		}
		inicialize(context,progress,mainWindow);

		renderKit.dispose(splash);
		renderKit.show(mainWindow);
	}

	public abstract String getUIEnvironmentName();

	public void inicialize (Context context , Progress progress,UIComponent mainWindow){

	}

	/**
	 * 
	 * @return Unrendered UIWindow
	 */
	public abstract UIComponent defineMainWindow(Context context);

	/**
	 * Splash window. If unrendered will be rendered 
	 * @return splash window.
	 */
	public abstract UIComponent defineSplashWindow(Context context);


}

package org.middleheaven.ui.desktop;

import org.middleheaven.progress.BoundProgress;
import org.middleheaven.progress.Progress;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.desktop.awt.Desktop;
import org.middleheaven.ui.models.DesktopClientModel;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

public class DesktopClientRender  extends UIRender{

	public boolean isChildrenRenderer() {
		return true;
	}
	
	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent, UIComponent component) {
		
		Desktop dclient = new Desktop();
		
		// parent is null has the client is a root element
		RenderKit renderKit = context.getRenderKit();
		
		DesktopClientModel clientModel = (DesktopClientModel) component.getUIModel();
		
		dclient.setUIModel(clientModel);
		
		Progress progress = new BoundProgress(component.getChildrenCount());

		context.setAttribute(ContextScope.RENDERING, "progress", progress);

		UIComponent splash = clientModel.defineSplashWindow((UIClient)component,context);
		
		// show progress
		if (splash!=null){
			if (!splash.isRendered()){
				splash = renderKit.renderComponent(context, dclient, splash);
			}
			splash.setUIParent(dclient);
			renderKit.show(splash);
			splash.gainFocus();
		}
		
		
		for (UIComponent comp : component.getChildrenComponents()){
			
			
			UIComponent renderedComponent = renderKit.renderComponent(context, dclient, comp);

			dclient.addComponent(renderedComponent);
			
			progress.increment();
		}
		
		
		renderKit.dispose(splash);
	
		return dclient;
	}

}

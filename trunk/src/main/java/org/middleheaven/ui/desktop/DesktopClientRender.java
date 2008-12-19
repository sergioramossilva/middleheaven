package org.middleheaven.ui.desktop;

import org.middleheaven.progress.BoundProgress;
import org.middleheaven.progress.Progress;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIDesktop;
import org.middleheaven.ui.models.DesktopClientModel;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

public class DesktopClientRender  extends UIRender{

	public boolean isChildrenRenderer() {
		return true;
	}
	
	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent, UIComponent client) {
		
		// parent is null has the client is a root element
		RenderKit renderKit = context.getRenderKit();
		
		DesktopClientModel clientModel = (DesktopClientModel) client.getUIModel();
		
		Progress progress = new BoundProgress(client.getChildrenCount());

		context.setAttribute(ContextScope.RENDERING, "progress", progress);

		UIComponent splash = clientModel.defineSplashWindow((UIDesktop)client,context);
		
		// show progress
		if (splash!=null){
			if (!splash.isRendered()){
				splash = renderKit.renderComponent(context, client, splash);
			}
			renderKit.show(splash);
		}
		
		for (UIComponent component : client.getChildrenComponents()){
			
			client.removeComponent(component);
			
			UIComponent renderedComponent = renderKit.renderComponent(context, client, component);

			client.addComponent(renderedComponent);
			
			progress.increment();
		}
		

		
		renderKit.dispose(splash);
	
		return client;
	}

}

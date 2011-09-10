package org.middleheaven.core.bootstrap.client;

import org.middleheaven.core.services.ServiceNotAvailableException;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.logging.Log;
import org.middleheaven.ui.MapContext;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIEnvironment;
import org.middleheaven.ui.UIEnvironmentType;
import org.middleheaven.ui.UIService;
import org.middleheaven.ui.components.UIDesktop;
import org.middleheaven.ui.models.DesktopClientModel;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.rendering.RenderingContext;


public class DesktopUIContainer extends AbstractStandaloneContainer {

	RenderingContext renderedContext;

	public DesktopUIContainer(ManagedFile repository) {
		super(repository);
	}

	public void start(){

		try{
			UIService uiService=ServiceRegistry.getService(UIService.class);
			
			UIEnvironment env = uiService.getDefaultUIEnvironment(UIEnvironmentType.DESKTOP);

			if (env.getClients().isEmpty()){
				throw new RuntimeException("No UIClients defined for environment" + env.getName());
			}
			
			UIClient client = env.getClients().iterator().next();
			
			final RenderKit renderKit = client.getUIModel().getRenderKit();
			
			this.renderedContext = new RenderingContext(new MapContext(),renderKit);
	
			client = renderKit.renderComponent(renderedContext, null, client);
			
			DesktopClientModel clientModel = (DesktopClientModel) client.getUIModel();
			
			
			UIComponent mainWindow;
			 if (client.getChildrenCount()>1){
				mainWindow = clientModel.defineMainWindow((UIDesktop)client,renderedContext);
			} else if (client.getChildrenCount()==0){
				Log.onBookFor(this.getClass()).error("No main window found");
				return;
			} else {
				mainWindow = client.getChildrenComponents().get(0);
			}

			renderKit.show(mainWindow);
			
		} catch (ServiceNotAvailableException e){
			Log.onBookFor(this.getClass()).warn("Executing without UI client");
		}

	}

	@Override
	public void stop() {
		try{
			UIService uiService=ServiceRegistry.getService(UIService.class);
			
			UIEnvironment env = uiService.getDefaultUIEnvironment(UIEnvironmentType.DESKTOP);

			if (env.getClients().isEmpty()){
				return;
			}
			
			UIClient client = env.getClients().iterator().next();
			
			final RenderKit renderKit = client.getUIModel().getRenderKit();
			
			DesktopClientModel clientModel = (DesktopClientModel) client.getUIModel();
			UIComponent mainWindow = clientModel.defineMainWindow((UIDesktop)client,renderedContext);

			renderKit.dispose(mainWindow);
			
		} catch (ServiceNotAvailableException e){
			Log.onBookFor(this.getClass()).trace("Stopping without UI client");
		}
	}


}

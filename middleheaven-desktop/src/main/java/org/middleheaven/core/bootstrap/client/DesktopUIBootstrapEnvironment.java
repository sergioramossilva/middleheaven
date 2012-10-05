package org.middleheaven.core.bootstrap.client;

import org.middleheaven.core.services.ServiceNotAvailableException;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.logging.Logger;
import org.middleheaven.process.MapContext;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIEnvironmentType;
import org.middleheaven.ui.UIService;
import org.middleheaven.ui.components.UIDesktop;
import org.middleheaven.ui.desktop.swing.SwingRenderKit;
import org.middleheaven.ui.models.UIClientModel;
import org.middleheaven.ui.rendering.RenderingContext;


public class DesktopUIBootstrapEnvironment extends AbstractStandaloneBootstrapEnvironment {

	RenderingContext renderedContext;
	private Logger logger;

	public DesktopUIBootstrapEnvironment(Logger logger) {
		super();
		this.logger = logger;
		this.renderedContext = new RenderingContext(new MapContext(), new SwingRenderKit());
	}

	public void start(){

		try{
			UIService uiService = ServiceRegistry.getService(UIService.class);
			
			UIClient client = uiService.getUIClientRendering(UIEnvironmentType.DESKTOP).getComponent();

			UIClientModel clientModel = (UIClientModel) client.getUIModel();
			
			
			UIComponent mainWindow;
			 if (client.getChildrenCount()>1){
				mainWindow = clientModel.resolveMainWindow((UIDesktop)client,renderedContext);
			} else if (client.getChildrenCount()==0){
				logger.error("No main window found");
				return;
			} else {
				mainWindow = client.getChildrenComponents().get(0);
			}

			 clientModel.getSceneNavigator().show(mainWindow);
			
		} catch (ServiceNotAvailableException e){
			logger.warn("Executing without UI client");
		}

	}

	@Override
	public void stop() {
		try{
			UIService uiService=ServiceRegistry.getService(UIService.class);
			
			UIClient client =  uiService.getUIClientRendering(UIEnvironmentType.DESKTOP).getComponent();
			
			final UIClientModel clientModel = client.getUIModel();
			
			UIComponent mainWindow = clientModel.resolveMainWindow((UIDesktop)client,renderedContext);

			clientModel.getSceneNavigator().dispose(mainWindow);
			
		} catch (ServiceNotAvailableException e){
			logger.trace("Stopping without UI client");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "desktop";
	}


}

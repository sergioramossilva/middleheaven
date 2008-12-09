package org.middleheaven.core.bootstrap.client;

import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.core.services.ServiceNotFoundException;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.logging.Logging;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIEnvironmentType;
import org.middleheaven.ui.UIService;
import org.middleheaven.ui.models.UIClientModel;


public class DesktopUIContainer extends StandaloneContainer {


	public DesktopUIContainer(ManagedFile repository) {
		super(repository);
	}

	public void init(ExecutionEnvironmentBootstrap bootstrap){

		try{
			UIService uiService=ServiceRegistry.getService(UIService.class);

			UIClient client = uiService.getUIClient(UIEnvironmentType.DESKTOP, "main");

			((UIClientModel)client.getUIModel()).execute(null); // TODO provide a context
		} catch (ServiceNotFoundException e){
			Logging.getBook(this.getClass()).warn("Executing without UI client");
		}

	}

	@Override
	public void stop(ExecutionEnvironmentBootstrap bootstrap) {
		// TODO Auto-generated method stub

	}


}

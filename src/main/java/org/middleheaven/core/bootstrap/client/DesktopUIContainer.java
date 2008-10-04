package org.middleheaven.core.bootstrap.client;

import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.core.services.ServiceNotFoundException;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.logging.Logging;
import org.middleheaven.ui.UIEnvironmentType;
import org.middleheaven.ui.UIService;
import org.middleheaven.ui.client.UIClient;


public class DesktopUIContainer extends StandaloneContainer {


	public DesktopUIContainer(ManagedFile repository) {
		super(repository);
	}

	public void init(ExecutionEnvironmentBootstrap bootstrap){

		try{
			UIService uiService=ServiceRegistry.getService(UIService.class);

			UIClient client = uiService.getUIClient(UIEnvironmentType.DESKTOP, "main");

			client.execute(null); // TODO provide a context
		} catch (ServiceNotFoundException e){
			Logging.getBook(this.getClass()).logWarn("Executing withou UI client");
		}

	}

	@Override
	public void stop(ExecutionEnvironmentBootstrap bootstrap) {
		// TODO Auto-generated method stub

	}


}

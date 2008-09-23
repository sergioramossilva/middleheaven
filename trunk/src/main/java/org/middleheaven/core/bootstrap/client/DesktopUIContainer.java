package org.middleheaven.core.bootstrap.client;

import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.ui.UIEnvironmentType;
import org.middleheaven.ui.UIService;
import org.middleheaven.ui.client.UIClient;


public class DesktopUIContainer extends StandaloneContainer {

    
    public DesktopUIContainer(ManagedFile repository) {
		super(repository);
	}

	public void init(ExecutionEnvironmentBootstrap bootstrap){

    	UIService uiService=ServiceRegistry.getService(UIService.class);
    	
    	UIClient client = uiService.getUIClient(UIEnvironmentType.DESKTOP, "main");
    	
    	client.execute(null); // TODO provide a context
    	
		
    }

	@Override
	public void stop(ExecutionEnvironmentBootstrap bootstrap) {
		// TODO Auto-generated method stub
		
	}


}

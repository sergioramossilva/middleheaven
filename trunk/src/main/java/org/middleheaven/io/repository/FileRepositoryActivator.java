package org.middleheaven.io.repository;

import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.io.repository.service.CommonRepositories;
import org.middleheaven.io.repository.service.FileRepositoryService;

public class FileRepositoryActivator extends ServiceActivator {

	private MapFileRepositoryService service = new MapFileRepositoryService();
	
	public FileRepositoryActivator(@Service BootstrapService bootstrapService ) {
		Container container = bootstrapService.getContainer(); 

		service.registerRepository(CommonRepositories.DATA, container.getAppDataRepository());
		service.registerRepository(CommonRepositories.APP_CONFIGURATION, container.getAppConfigRepository());
		service.registerRepository(CommonRepositories.ENV_CONFIGURATION, container.getEnvironmentConfigRepository());
		service.registerRepository(CommonRepositories.LOG, container.getAppLogRepository());

	}

	@Override
	public void activate(ServiceContext context) {
		
		context.register(FileRepositoryService.class, service, null); 

	}

	@Override
	public void inactivate(ServiceContext context) {
		// no-op
	}

}

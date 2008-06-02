package org.middleheaven.io.repository;

import org.middleheaven.core.Container;
import org.middleheaven.core.services.ContainerService;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.core.services.engine.ServiceActivator;
import org.middleheaven.io.repository.service.CommonRepositories;
import org.middleheaven.io.repository.service.FileRepositoryService;

public class FileRepositoryActivator extends ServiceActivator {

	private MapFileRepositoryService service = new MapFileRepositoryService();

	public FileRepositoryActivator() {}

	@Override
	public void activate(ServiceContext context) {
		Container container = ServiceRegistry.getService(ContainerService.class).getContainer(); 

		service.registerRepository(CommonRepositories.DATA, container.getAppDataRepository());
		service.registerRepository(CommonRepositories.APP_CONFIGURATION, container.getAppConfigRepository());
		service.registerRepository(CommonRepositories.ENV_CONFIGURATION, container.getEnvironmentConfigRepository());
		service.registerRepository(CommonRepositories.LOG, container.getAppLogRepository());

		ServiceRegistry.register(FileRepositoryService.class, service, null); 

	}

	@Override
	public void inactivate(ServiceContext context) {
		// no-op
	}

}

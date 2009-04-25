package org.middleheaven.io.repository;

import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.io.repository.service.CommonRepositories;
import org.middleheaven.io.repository.service.FileRepositoryService;

public class FileRepositoryActivator extends ServiceActivator {

	private MapFileRepositoryService fileRepositoryService = new MapFileRepositoryService();
	
	private  BootstrapService bootstrapService;
	
	@Wire
	public void setBootstrapService(BootstrapService bootstrapService) {
		this.bootstrapService = bootstrapService;
	}
	
	@Publish
	public FileRepositoryService getFileRepositoryService() {
		return fileRepositoryService;
	}

	public FileRepositoryActivator() {
	
	}

	@Override
	public void activate(ServiceAtivatorContext context) {
		Container container = bootstrapService.getEnvironmentBootstrap().getContainer(); 

		fileRepositoryService.registerRepository(CommonRepositories.DATA, container.getAppDataRepository());
		fileRepositoryService.registerRepository(CommonRepositories.APP_CONFIGURATION, container.getAppConfigRepository());
		fileRepositoryService.registerRepository(CommonRepositories.ENV_CONFIGURATION, container.getEnvironmentConfigRepository());
		fileRepositoryService.registerRepository(CommonRepositories.LOG, container.getAppLogRepository());


	}

	@Override
	public void inactivate(ServiceAtivatorContext context) {

		// no-op
		fileRepositoryService = null;
	}

}

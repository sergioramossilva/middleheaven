package org.middleheaven.io.repository;

import org.middleheaven.core.BootstrapContainer;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.io.repository.service.CommonRepositories;
import org.middleheaven.io.repository.service.FileRepositoryRegistryService;

public class FileRepositoryActivator extends Activator {

	private MapFileRepositoryService fileRepositoryService = new MapFileRepositoryService();
	
	private  BootstrapService bootstrapService;
	
	@Wire
	public void setBootstrapService(BootstrapService bootstrapService) {
		this.bootstrapService = bootstrapService;
	}
	
	@Publish
	public FileRepositoryRegistryService getFileRepositoryService() {
		return fileRepositoryService;
	}

	public FileRepositoryActivator() {
	
	}

	@Override
	public void activate(ActivationContext context) {
		BootstrapContainer container = bootstrapService.getEnvironmentBootstrap().getContainer(); 

		fileRepositoryService.registerRepository(CommonRepositories.APP_DATA, container.getAppDataRepository());
		fileRepositoryService.registerRepository(CommonRepositories.APP_CLASSPATH, container.getAppClasspathRepository());
		fileRepositoryService.registerRepository(CommonRepositories.APP_ROOT, container.getAppRootRepository());
		fileRepositoryService.registerRepository(CommonRepositories.APP_CONFIGURATION, container.getAppConfigRepository());
		fileRepositoryService.registerRepository(CommonRepositories.APP_LOG, container.getAppLogRepository());
		
		fileRepositoryService.registerRepository(CommonRepositories.ENV_CONFIGURATION, container.getEnvironmentConfigRepository());
		fileRepositoryService.registerRepository(CommonRepositories.ENV_DATA, container.getEnvironmentConfigRepository());
		
	}

	@Override
	public void inactivate(ActivationContext context) {

		// no-op
		fileRepositoryService = null;
	}

}

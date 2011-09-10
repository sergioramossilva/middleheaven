package org.middleheaven.io.repository.container;

import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.bootstrap.ContainerFileSystem;
import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Wire;

public class AppFoldersRepositoryActivator extends Activator {

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

	public AppFoldersRepositoryActivator() {
	
	}

	@Override
	public void activate(ActivationContext context) {
		ContainerFileSystem fileSystem = bootstrapService.getEnvironmentBootstrap().getContainer().getFileSystem();

		fileRepositoryService.registerRepository(CommonRepositories.APP_DATA, fileSystem.getAppDataRepository());
		fileRepositoryService.registerRepository(CommonRepositories.APP_CLASSPATH, fileSystem.getAppClasspathRepository());
		fileRepositoryService.registerRepository(CommonRepositories.APP_ROOT, fileSystem.getAppRootRepository());
		fileRepositoryService.registerRepository(CommonRepositories.APP_CONFIGURATION, fileSystem.getAppConfigRepository());
		fileRepositoryService.registerRepository(CommonRepositories.APP_LOG, fileSystem.getAppLogRepository());
		
		fileRepositoryService.registerRepository(CommonRepositories.ENV_CONFIGURATION, fileSystem.getEnvironmentConfigRepository());
		fileRepositoryService.registerRepository(CommonRepositories.ENV_DATA, fileSystem.getEnvironmentConfigRepository());
		
	}

	@Override
	public void inactivate(ActivationContext context) {

		// no-op
		fileRepositoryService = null;
	}

}

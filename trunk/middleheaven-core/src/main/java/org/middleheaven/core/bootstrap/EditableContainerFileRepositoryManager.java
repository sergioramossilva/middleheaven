package org.middleheaven.core.bootstrap;

import org.middleheaven.io.repository.ManagedFile;

public class EditableContainerFileRepositoryManager implements FileContext {

	
	private ManagedFile appClasspathRepository;
	private ManagedFile appConfigRepository;
	private ManagedFile appDataRepository;
	private ManagedFile appLogRepository;
	private ManagedFile appRootRepository;
	private ManagedFile environmentConfigRepository;
	private ManagedFile environmentDataRepository;

	public EditableContainerFileRepositoryManager(){
		
	}
	@Override
	public ManagedFile getAppClasspathRepository() {
		return this.appClasspathRepository;
	}

	@Override
	public ManagedFile getAppConfigRepository() {
		return this.appConfigRepository;
	}

	@Override
	public ManagedFile getAppDataRepository() {
		return this.appDataRepository;
	}

	@Override
	public ManagedFile getAppLogRepository() {
		return this.appLogRepository;
	}

	@Override
	public ManagedFile getAppRootRepository() {
		return this.appRootRepository;
	}

	@Override
	public ManagedFile getEnvironmentConfigRepository() {
		return this.environmentConfigRepository;
	}

	@Override
	public ManagedFile getEnvironmentDataRepository() {
		return this.environmentDataRepository;
	}
	public void setAppClasspathRepository(ManagedFile appClasspathRepository) {
		this.appClasspathRepository = appClasspathRepository;
	}
	public void setAppConfigRepository(ManagedFile appConfigRepository) {
		this.appConfigRepository = appConfigRepository;
	}
	public void setAppDataRepository(ManagedFile appDataRepository) {
		this.appDataRepository = appDataRepository;
	}
	public void setAppLogRepository(ManagedFile appLogRepository) {
		this.appLogRepository = appLogRepository;
	}
	public void setAppRootRepository(ManagedFile appRootRepository) {
		this.appRootRepository = appRootRepository;
	}
	public void setEnvironmentConfigRepository(
			ManagedFile environmentConfigRepository) {
		this.environmentConfigRepository = environmentConfigRepository;
	}
	public void setEnvironmentDataRepository(ManagedFile environmentDataRepository) {
		this.environmentDataRepository = environmentDataRepository;
	}
	
	

}

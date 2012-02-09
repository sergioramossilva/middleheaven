package org.middleheaven.tool.test;

import org.middleheaven.core.bootstrap.BootstrapContainer;
import org.middleheaven.core.bootstrap.ExecutionEnvironmentBootstrap;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.machine.MachineFiles;

public class TestBootstrap extends ExecutionEnvironmentBootstrap {

	BootstrapContainer container;
	
	public TestBootstrap(){
		container = new TestContainer(MachineFiles.getDefaultFolder());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ManagedFile getEnvironmentRootFolder() {
		return MachineFiles.getDefaultFolder();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public BootstrapContainer resolveContainer(ManagedFile rooFolder) {
		return container;
	}



}
